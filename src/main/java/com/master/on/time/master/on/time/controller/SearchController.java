package com.master.on.time.master.on.time.controller;

import com.master.on.time.master.on.time.dto.UserResponseDto;
import com.master.on.time.master.on.time.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(summary = "Search specialists",
            description = "Search for specialists by optional"
                    + " service type and/or location. Requires authentication.")
    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public List<UserResponseDto> searchSpecialists(
            @Parameter(description = "Type of service to search for", example = "Plumbing")
            @RequestParam(required = false) String serviceType,

            @Parameter(description = "Location to filter specialists", example = "Kyiv")
            @RequestParam(required = false) String location
    ) {
        return searchService.searchSpecialists(serviceType, location);
    }
}
