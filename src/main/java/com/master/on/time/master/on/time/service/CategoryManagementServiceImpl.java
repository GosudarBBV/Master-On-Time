package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.CategoryItemResponseDto;
import com.master.on.time.master.on.time.dto.CategoryResponseDto;
import com.master.on.time.master.on.time.exception.EntityNotFoundException;
import com.master.on.time.master.on.time.exception.UserRoleNotAllowedException;
import com.master.on.time.master.on.time.mapper.CategoryMapper;
import com.master.on.time.master.on.time.model.Category;
import com.master.on.time.master.on.time.model.CategoryItem;
import com.master.on.time.master.on.time.model.RoleName;
import com.master.on.time.master.on.time.model.User;
import com.master.on.time.master.on.time.repository.CategoryItemRepository;
import com.master.on.time.master.on.time.repository.CategoryRepository;
import com.master.on.time.master.on.time.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryManagementServiceImpl implements CategoryManagementService {

    private final CategoryRepository categoryRepository;
    private final CategoryItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponseDto createCategory(Long specialistId, String categoryName) {
        User specialist = getVerifiedSpecialist(specialistId);

        Category category = new Category();
        category.setName(categoryName);
        category.setSpecialist(specialist);

        Category saved = categoryRepository.save(category);
        return categoryMapper.toDto(saved);
    }

    @Override
    public CategoryResponseDto updateCategory(Long categoryId, String categoryName) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        category.setName(categoryName);
        Category updated = categoryRepository.save(category);
        return categoryMapper.toDto(updated);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public CategoryItemResponseDto addItem(Long categoryId, String itemName,
                                           int durationMinutes, double price) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        CategoryItem item = new CategoryItem();
        item.setName(itemName);
        item.setDurationMinutes(durationMinutes);
        item.setPrice(price);
        item.setCategory(category);

        CategoryItem saved = itemRepository.save(item);
        return categoryMapper.toItemDto(saved);
    }

    @Override
    public CategoryItemResponseDto updateItem(Long itemId, String itemName,
                                              int durationMinutes, double price) {
        CategoryItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        item.setName(itemName);
        item.setDurationMinutes(durationMinutes);
        item.setPrice(price);

        CategoryItem updated = itemRepository.save(item);
        return categoryMapper.toItemDto(updated);
    }

    @Override
    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public List<CategoryResponseDto> getCategoriesBySpecialist(Long specialistId) {
        List<Category> categories = categoryRepository.findBySpecialistId(specialistId);
        return categories.stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    private User getVerifiedSpecialist(Long providerId) {
        User user = userRepository.findById(providerId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        boolean isSpecialist = user.getRoles().stream()
                .anyMatch(role -> role.getName() == RoleName.SPECIALIST);
        if (!isSpecialist) {
            throw new UserRoleNotAllowedException("Only specialists can "
                    + "manage categories and items");
        }
        return user;
    }
}
