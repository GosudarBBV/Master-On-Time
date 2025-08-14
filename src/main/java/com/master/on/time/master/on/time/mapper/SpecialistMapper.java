package com.master.on.time.master.on.time.mapper;

import com.master.on.time.master.on.time.config.MapperConfig;
import com.master.on.time.master.on.time.dto.SpecialistApplicationDto;
import com.master.on.time.master.on.time.model.Specialist;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface SpecialistMapper {
    SpecialistApplicationDto toApplicationDto(Specialist specialist);
}
