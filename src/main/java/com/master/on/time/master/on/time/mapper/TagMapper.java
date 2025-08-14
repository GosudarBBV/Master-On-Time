package com.master.on.time.master.on.time.mapper;

import com.master.on.time.master.on.time.config.MapperConfig;
import com.master.on.time.master.on.time.dto.CreateTagRequestDto;
import com.master.on.time.master.on.time.dto.TagDto;
import com.master.on.time.master.on.time.model.Tag;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface TagMapper {
    TagDto toDto(Tag tag);

    Tag toEntity(CreateTagRequestDto request);
}
