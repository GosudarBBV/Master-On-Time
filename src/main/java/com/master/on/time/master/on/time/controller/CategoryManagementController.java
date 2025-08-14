package com.master.on.time.master.on.time.controller;

import com.master.on.time.master.on.time.dto.CategoryItemResponseDto;
import com.master.on.time.master.on.time.dto.CategoryResponseDto;
import com.master.on.time.master.on.time.service.CategoryManagementService;
import com.master.on.time.master.on.time.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/specialist/categories")
@RequiredArgsConstructor
@Tag(name = "Specialist: Category Management",
        description = "Endpoints for managing categories and category items")
public class CategoryManagementController {

    private final CategoryManagementService categoryManagementService;
    private final UserService userService;

    @PreAuthorize("hasRole('SPECIALIST')")
    @PostMapping
    @Operation(summary = "Create a new category",
            description = "Create a new category for the authenticated specialist")
    public CategoryResponseDto createCategory(@RequestParam String name) {
        Long userId = userService.getAuthenticatedUserId();
        return categoryManagementService.createCategory(userId, name);
    }

    @PreAuthorize("hasRole('SPECIALIST')")
    @PutMapping("/{id}")
    @Operation(summary = "Update a category",
            description = "Update the name of an existing category")
    public CategoryResponseDto updateCategory(
            @PathVariable Long id,
            @RequestParam String name
    ) {
        return categoryManagementService.updateCategory(id, name);
    }

    @PreAuthorize("hasRole('SPECIALIST')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category",
            description = "Delete a category by its ID")
    public void deleteCategory(@PathVariable Long id) {
        categoryManagementService.deleteCategory(id);
    }

    @PreAuthorize("hasRole('SPECIALIST')")
    @PostMapping("/{categoryId}/items")
    @Operation(summary = "Add item to category",
            description = "Add a new item to a specific category")
    public CategoryItemResponseDto addItem(
            @PathVariable Long categoryId,
            @RequestParam String name,
            @RequestParam int durationMinutes,
            @RequestParam double price
    ) {
        return categoryManagementService.addItem(categoryId, name, durationMinutes, price);
    }

    @PreAuthorize("hasRole('SPECIALIST')")
    @PutMapping("/items/{id}")
    @Operation(summary = "Update category item",
            description = "Update details of a category item")
    public CategoryItemResponseDto updateItem(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam int durationMinutes,
            @RequestParam double price
    ) {
        return categoryManagementService.updateItem(id, name, durationMinutes, price);
    }

    @PreAuthorize("hasRole('SPECIALIST')")
    @DeleteMapping("/items/{id}")
    @Operation(summary = "Delete category item",
            description = "Delete a category item by its ID")
    public void deleteItem(@PathVariable Long id) {
        categoryManagementService.deleteItem(id);
    }

    @PreAuthorize("hasRole('SPECIALIST')")
    @GetMapping
    @Operation(summary = "Get categories",
            description = "Get all categories for the authenticated specialist")
    public List<CategoryResponseDto> getCategories() {
        Long userId = userService.getAuthenticatedUserId();
        return categoryManagementService.getCategoriesBySpecialist(userId);
    }
}
