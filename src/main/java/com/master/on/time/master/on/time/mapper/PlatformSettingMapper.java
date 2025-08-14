package com.master.on.time.master.on.time.mapper;

import com.master.on.time.master.on.time.config.MapperConfig;
import com.master.on.time.master.on.time.dto.PlatformSettingDto;
import com.master.on.time.master.on.time.model.PlatformSetting;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface PlatformSettingMapper {
    PlatformSettingDto toDto(PlatformSetting setting);

    PlatformSetting toEntity(PlatformSettingDto dto);

    List<PlatformSettingDto> toDtoList(List<PlatformSetting> settings);
}
