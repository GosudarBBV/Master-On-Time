package com.master.on.time.master.on.time.controller;

import com.master.on.time.master.on.time.service.PortfolioService;
import com.master.on.time.master.on.time.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
@Tag(name = "Portfolio", description = "Manage specialist portfolio images")
public class PortfolioController {
    private final PortfolioService portfolioService;
    private final UserService userService;

    @PreAuthorize("hasRole('SPECIALIST')")
    @Operation(summary = "Upload a portfolio image")
    @PostMapping("/upload")
    public String uploadPortfolioImage(@RequestParam("file") MultipartFile file) {
        Long userId = userService.getAuthenticatedUserId();
        return portfolioService.savePortfolioImage(userId, file);
    }

    @PreAuthorize("hasRole('SPECIALIST')")
    @Operation(summary = "Get portfolio images of authenticated specialist")
    @GetMapping("/me")
    public List<String> getMyPortfolioImages() {
        Long userId = userService.getAuthenticatedUserId();
        return portfolioService.getPortfolioImagesUrls(userId);
    }
}
