package com.master.on.time.master.on.time.controller;

import com.master.on.time.master.on.time.dto.FavoriteDto;
import com.master.on.time.master.on.time.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@Tag(name = "Favorites", description = "Endpoints to manage favorite users")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PreAuthorize("hasRole('USER') or hasRole('SPECIALIST')")
    @PostMapping("/{targetId}")
    @Operation(summary = "Add user to favorites")
    public void addToFavorites(@PathVariable Long targetId) {
        favoriteService.addToFavorites(targetId);
    }

    @PreAuthorize("hasRole('USER') or hasRole('SPECIALIST')")
    @DeleteMapping("/{targetId}")
    @Operation(summary = "Remove user from favorites")
    public void removeFromFavorites(@PathVariable Long targetId) {
        favoriteService.removeFromFavorites(targetId);
    }

    @PreAuthorize("hasRole('USER') or hasRole('SPECIALIST')")
    @GetMapping
    @Operation(summary = "Get all favorite users for the current client")
    public List<FavoriteDto> getFavorites() {
        return favoriteService.getFavorites();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get favorite specialists of a specific client by client ID")
    @GetMapping("/client/{clientId}")
    public List<FavoriteDto> getFavoritesByClientId(
            @Parameter(description = "ID of the client") @PathVariable Long clientId) {
        return favoriteService.getFavoritesByClientId(clientId);
    }
}
