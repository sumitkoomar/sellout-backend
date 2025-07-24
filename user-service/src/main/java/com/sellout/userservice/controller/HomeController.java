package com.sellout.userservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/AopAndInterceptor")
public class HomeController {

    @GetMapping("/user")
    public void homepage(){
        System.out.println("Home page");
    }
}
