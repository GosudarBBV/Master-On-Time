package com.master.on.time.master.on.time.controller;

import com.master.on.time.master.on.time.dto.UserResponseDto;
import com.master.on.time.master.on.time.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/specialists")
@Tag(name = "Specialist Search", description = "Search for specialists")
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "Search specialists with filters")
    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public List<UserResponseDto> searchSpecialists(
            @RequestParam(required = false) String serviceName,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) List<String> categories,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Double minRating
    ) {
        return searchService.searchSpecialists(serviceName,
                firstName, city, categories,
                minExperience, minRating);
    }
}
