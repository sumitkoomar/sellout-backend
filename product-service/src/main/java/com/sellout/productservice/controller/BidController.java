package com.sellout.productservice.controller;

import com.sellout.productservice.dto.BidMessage;
import com.sellout.productservice.kafka.BidProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/bids")
public class BidController {

    private final BidProducer bidProducer;

    public BidController(BidProducer bidProducer) {
        this.bidProducer = bidProducer;
    }

    @PostMapping
    public ResponseEntity<String> placeBid(@RequestBody BidMessage bidMessage) {
        bidMessage.setTimestamp(LocalDateTime.now());
        bidProducer.sendBid(bidMessage);
        return ResponseEntity.ok("Bid placed successfully");
    }
}
