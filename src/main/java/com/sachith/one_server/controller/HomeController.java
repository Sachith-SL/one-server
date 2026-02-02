package com.sachith.one_server.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/one/v1")
public class HomeController {
    @GetMapping
    public String home() {
        return "Welcome to Practical One Application!";
    }

    @GetMapping("/health")
    public String health() {
        return "Welcome to Practical One Application!";
    }
}
