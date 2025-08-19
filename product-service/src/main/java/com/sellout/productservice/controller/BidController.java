package com.sellout.productservice.controller;

import com.sellout.productservice.dto.BidMessage;
import com.sellout.productservice.dto.ProductResponse;
import com.sellout.productservice.entity.Product;
import com.sellout.productservice.kafka.BidProducer;
import com.sellout.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/place-bid")
    public ResponseEntity<String> placeBid(@RequestBody BidMessage bidMessage) {
        bidMessage.setTimestamp(LocalDateTime.now());
        bidProducer.sendBid(bidMessage);
        return ResponseEntity.ok("Bid placed successfully");
    }
}
