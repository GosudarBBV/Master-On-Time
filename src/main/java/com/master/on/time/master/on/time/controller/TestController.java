package com.master.on.time.master.on.time.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
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

    @GetMapping("/headers")
    public String headers(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            sb.append(name).append(": ").append(request.getHeader(name)).append("\n");
        }
        return sb.toString();
    }
}
