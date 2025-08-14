package com.master.on.time.master.on.time.mapper;

import com.master.on.time.master.on.time.config.MapperConfig;
import com.master.on.time.master.on.time.dto.AvailabilityRequestDto;
import com.master.on.time.master.on.time.dto.UnavailabilityRequestDto;
import com.master.on.time.master.on.time.model.Availability;
import com.master.on.time.master.on.time.model.Unavailability;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapperConfig.class)
public interface ScheduleMapper {
    ScheduleMapper INSTANCE = Mappers.getMapper(ScheduleMapper.class);

    AvailabilityRequestDto toAvailabilityDto(Availability availability);

    Availability toAvailabilityEntity(AvailabilityRequestDto dto);

    UnavailabilityRequestDto toUnavailabilityDto(Unavailability unavailability);

    Unavailability toUnavailabilityEntity(UnavailabilityRequestDto dto);
}
