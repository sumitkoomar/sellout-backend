package com.sellout.productservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BidMessage {

    private Long productId;
    private Long bidderId;
    private BigDecimal amount;
    private LocalDateTime timestamp;

    public BidMessage() {
    }

    public BidMessage(Long productId, Long bidderId, BigDecimal amount, LocalDateTime timestamp) {
        this.productId = productId;
        this.bidderId = bidderId;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getBidderId() {
        return bidderId;
    }

    public void setBidderId(Long bidderId) {
        this.bidderId = bidderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
