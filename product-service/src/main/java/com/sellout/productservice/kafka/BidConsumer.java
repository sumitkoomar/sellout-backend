package com.sellout.productservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sellout.productservice.cache.RedisKeys;
import com.sellout.productservice.dto.BidMessage;
import com.sellout.productservice.entity.Product;
import com.sellout.productservice.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class BidConsumer {

    private static final Logger log = LoggerFactory.getLogger(BidConsumer.class);

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate redis;

    public BidConsumer(ProductRepository productRepository,
                       ObjectMapper objectMapper,
                       StringRedisTemplate redis) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
        this.redis = redis;
    }

    @KafkaListener(topics = "product-bids", groupId = "product-service-group")
    public void consume(String message) {
        try {
            BidMessage bid = objectMapper.readValue(message, BidMessage.class);
            log.info("📥 Received bid: {}", bid);

            Long productId = bid.getProductId();

            // 1️⃣ Read from Redis first (fast path)
            String priceStr = redis.opsForValue().get(RedisKeys.currentPrice(productId));
            String activeStr = redis.opsForValue().get(RedisKeys.activeFlag(productId));

            BigDecimal currentPrice;
            boolean active;

            if (priceStr != null && activeStr != null) {
                currentPrice = new BigDecimal(priceStr);
                active = "1".equals(activeStr);
                log.info("product details is stored in cache");
            } else {
                // Redis miss → load from DB
                Optional<Product> optionalProduct = productRepository.findById(productId);
                if (optionalProduct.isEmpty()) {
                    log.warn("❌ Product not found for id {}", productId);
                    return;
                }
                Product product = optionalProduct.get();

                currentPrice = product.getCurrentPrice();
                active = product.isActive();

                // seed Redis
                redis.opsForValue().set(RedisKeys.currentPrice(productId), currentPrice.toPlainString());
                redis.opsForValue().set(RedisKeys.activeFlag(productId), active ? "1" : "0");
                redis.opsForValue().set(RedisKeys.highestBidder(productId), "");
            }

            // 2️⃣ Validate bid
            if (active && bid.getAmount().compareTo(currentPrice) > 0) {
                // Update Redis
                redis.opsForValue().set(RedisKeys.currentPrice(productId), bid.getAmount().toPlainString());
                redis.opsForValue().set(RedisKeys.highestBidder(productId), bid.getBidderId().toString());

                // Update DB (async/secondary system might be better for scaling)
                productRepository.findById(productId).ifPresent(product -> {
                    product.setCurrentPrice(bid.getAmount());
                    product.setLastUpdatedTime();
                    productRepository.save(product);
                });

                log.info("✅ Bid accepted. Product {} price updated to {}", productId, bid.getAmount());
            } else {
                log.info("❌ Bid rejected for product {}: amount too low or inactive", productId);
            }

        } catch (Exception e) {
            log.error("🚨 Failed to process bid message", e);
        }
    }
}
