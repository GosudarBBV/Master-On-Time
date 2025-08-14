package com.master.on.time.master.on.time.mapper;

import com.master.on.time.master.on.time.config.MapperConfig;
import com.master.on.time.master.on.time.dto.NotificationDto;
import com.master.on.time.master.on.time.model.Notification;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface NotificationMapper {
    NotificationDto toDto(Notification notification);
}
