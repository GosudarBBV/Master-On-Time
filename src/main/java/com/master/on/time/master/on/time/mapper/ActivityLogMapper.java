package com.master.on.time.master.on.time.mapper;

import com.master.on.time.master.on.time.config.MapperConfig;
import com.master.on.time.master.on.time.dto.ActivityLogDto;
import com.master.on.time.master.on.time.model.ActivityLog;
import java.time.LocalDateTime;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface ActivityLogMapper {
    ActivityLogDto toDto(ActivityLog log);

    ActivityLog toEntity(ActivityLogDto dto);

    default ActivityLogDto toUpdatedDto(ActivityLogDto dto) {
        ActivityLogDto updatedDto = new ActivityLogDto(
                dto.id(),
                LocalDateTime.now(),
                dto.userId(),
                dto.username(),
                dto.role(),
                dto.actionType(),
                dto.affectedEntityType(),
                dto.affectedEntityId(),
                dto.description(),
                dto.ipAddress()
        );
        return updatedDto;
    }

    List<ActivityLogDto> toDtoList(List<ActivityLog> logs);
}
