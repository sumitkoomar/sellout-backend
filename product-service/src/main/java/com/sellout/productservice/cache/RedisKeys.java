package com.sellout.productservice.cache;

public final class RedisKeys {
    private RedisKeys() {}

    public static String currentPrice(Long productId) {
        return "product:" + productId + ":currentPrice";
    }
    public static String highestBidder(long productId) {
        return "product:" + productId + ":highestBidder";
    }
    public static String bidsZSet(long productId) {
        return "product:" + productId + ":bids";
    }
    public static String activeFlag(long productId) {
        return "product:" + productId + ":active";
    }
}
