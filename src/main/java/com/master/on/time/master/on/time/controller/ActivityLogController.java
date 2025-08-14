package com.master.on.time.master.on.time.controller;

import com.master.on.time.master.on.time.dto.ActivityLogDto;
import com.master.on.time.master.on.time.service.ActivityLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/logs")
@RequiredArgsConstructor
@Tag(name = "Admin: Activity Logs", description = "Endpoints for"
        + " viewing and filtering admin activity logs")
public class ActivityLogController {

    private final ActivityLogService logService;

    @GetMapping
    @Operation(
            summary = "Get activity logs",
            description = "Retrieve paginated list of activity logs with "
                    + "optional filtering by date, username, action type, and affected entity"
    )
    public Page<ActivityLogDto> getLogs(
            @Parameter(description = "Start date for filtering logs "
                    + "(ISO format: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,

            @Parameter(description = "End date for filtering logs "
                    + "(ISO format: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,

            @Parameter(description = "Username of the user who performed the action")
            @RequestParam(required = false) String username,

            @Parameter(description = "Type of action performed (e.g., CREATE, UPDATE, DELETE)")
            @RequestParam(required = false) String actionType,

            @Parameter(description = "Entity affected by the action")
            @RequestParam(required = false) String affectedEntity,

            @Parameter(description = "Page number (0-based index)")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of records per page")
            @RequestParam(defaultValue = "20") int size
    ) {
        return logService.getLogs(
                startDate,
                endDate,
                username,
                actionType,
                affectedEntity,
                PageRequest.of(page, size)
        );
    }
}
