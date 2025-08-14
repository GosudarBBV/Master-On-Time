package com.master.on.time.master.on.time.mapper;

import com.master.on.time.master.on.time.config.MapperConfig;
import com.master.on.time.master.on.time.dto.CategoryItemResponseDto;
import com.master.on.time.master.on.time.dto.CategoryResponseDto;
import com.master.on.time.master.on.time.dto.CreateCategoryRequestDto;
import com.master.on.time.master.on.time.model.Category;
import com.master.on.time.master.on.time.model.CategoryItem;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {

    CategoryItemResponseDto toItemDto(CategoryItem item);

    CategoryResponseDto toDto(Category category);

    Category toEntity(CreateCategoryRequestDto request);
}
