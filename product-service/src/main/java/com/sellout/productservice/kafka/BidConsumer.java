package com.sellout.productservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sellout.productservice.dto.BidMessage;
import com.sellout.productservice.entity.Product;
import com.sellout.productservice.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BidConsumer {

    private static final Logger log = LoggerFactory.getLogger(BidConsumer.class);

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    public BidConsumer(ProductRepository productRepository, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "product-bids", groupId = "product-service-group")
    public void consume(String message) {
        try {
            BidMessage bid = objectMapper.readValue(message, BidMessage.class);
            log.info("Received bid: {}", bid);

            Optional<Product> optionalProduct = productRepository.findById(bid.getProductId());

            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();

                if (product.isActive() && bid.getAmount().compareTo(product.getCurrentPrice()) > 0) {
                    product.setCurrentPrice(bid.getAmount());
                    productRepository.save(product);
                    log.info("Product price updated to: {}", product.getCurrentPrice());
                }
            }

        } catch (Exception e) {
            log.error("Failed to process bid message", e);
        }
    }
}
