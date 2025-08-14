package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.ActivityLogDto;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActivityLogService {
    void recordLog(ActivityLogDto dto);

    Page<ActivityLogDto> getLogs(LocalDateTime startDate,
                                 LocalDateTime endDate,
                                 String username, String actionType,
                                 String affectedEntity,
                                 Pageable pageable);
}
