package com.master.on.time.master.on.time.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/random-message")
    public Map<String, String> getRandomMessage() {
        return Map.of("message", "Hello! This is a random message from the backend.");
    }
}
