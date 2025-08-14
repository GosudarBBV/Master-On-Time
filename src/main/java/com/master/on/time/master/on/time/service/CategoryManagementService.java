package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.CategoryItemResponseDto;
import com.master.on.time.master.on.time.dto.CategoryResponseDto;
import java.util.List;

public interface CategoryManagementService {
    CategoryResponseDto createCategory(Long specialistId, String categoryName);

    CategoryResponseDto updateCategory(Long categoryId, String categoryName);

    void deleteCategory(Long categoryId);

    CategoryItemResponseDto addItem(Long categoryId, String itemName,
                                    int durationMinutes, double price);

    CategoryItemResponseDto updateItem(Long itemId, String itemName,
                                       int durationMinutes, double price);

    void deleteItem(Long itemId);

    List<CategoryResponseDto> getCategoriesBySpecialist(Long specialistId);
}
