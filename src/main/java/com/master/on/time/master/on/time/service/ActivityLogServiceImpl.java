package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.ActivityLogDto;
import com.master.on.time.master.on.time.mapper.ActivityLogMapper;
import com.master.on.time.master.on.time.model.ActivityLog;
import com.master.on.time.master.on.time.repository.ActivityLogRepository;
import com.master.on.time.master.on.time.specification.ActivityLogSpecification;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogRepository logRepository;
    private final ActivityLogMapper logMapper;

    @Override
    public void recordLog(ActivityLogDto dto) {
        ActivityLogDto updatedDto = logMapper.toUpdatedDto(dto);
        ActivityLog log = logMapper.toEntity(updatedDto);
        logRepository.save(log);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityLogDto> getLogs(LocalDateTime startDate, LocalDateTime endDate,
                                        String username, String actionType,
                                        String affectedEntity, Pageable pageable) {

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate cannot be after endDate");
        }

        Specification<ActivityLog> spec = Specification
                .where(ActivityLogSpecification.hasTimestampAfter(startDate))
                .and(ActivityLogSpecification.hasTimestampBefore(endDate))
                .and(ActivityLogSpecification.hasUsername(username))
                .and(ActivityLogSpecification.hasActionType(actionType))
                .and(ActivityLogSpecification.hasAffectedEntity(affectedEntity));

        Page<ActivityLog> logsPage = logRepository.findAll(spec, pageable);
        return logsPage.map(logMapper::toDto);
    }
}
