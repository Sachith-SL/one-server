package com.sachith.one_server.controller;

import com.sachith.one_server.dto.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/one/v1")
public class HomeController {
    @GetMapping
    public ResponseEntity<BaseResponse<String>> home() {
        return ResponseEntity.ok(BaseResponse.success("connected", "Welcome to Practical One Application!"));
    }

    @GetMapping("/health")
    public String health() {
        return "Welcome to Practical One Application!";
    }
}
