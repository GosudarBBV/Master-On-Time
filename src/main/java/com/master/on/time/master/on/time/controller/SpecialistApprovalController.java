package com.master.on.time.master.on.time.controller;

import com.master.on.time.master.on.time.dto.SpecialistApplicationDto;
import com.master.on.time.master.on.time.dto.SpecialistDecisionRequestDto;
import com.master.on.time.master.on.time.service.SpecialistApprovalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/specialist-approvals")
@RequiredArgsConstructor
@Tag(name = "Specialist Approvals (Admin)",
        description = "Manage approval/rejection of specialist applications")
@PreAuthorize("hasRole('ADMIN')")
public class SpecialistApprovalController {

    private final SpecialistApprovalService specialistApprovalService;

    @GetMapping("/pending")
    @Operation(summary = "Get all pending specialist applications")
    public List<SpecialistApplicationDto> getPendingApplications() {
        return specialistApprovalService.getPendingApplications();
    }

    @GetMapping("/{specialistId}")
    @Operation(summary = "Get details of a specific specialist application")
    public SpecialistApplicationDto getApplicationDetails(@PathVariable Long specialistId) {
        return specialistApprovalService.getApplicationDetails(specialistId);
    }

    @PostMapping("/{specialistId}/approve")
    @Operation(summary = "Approve a specialist application")
    public void approveSpecialist(
            @PathVariable Long specialistId,
            @RequestBody(required = false) SpecialistDecisionRequestDto decisionDto
    ) {
        if (decisionDto == null) {
            decisionDto = new SpecialistDecisionRequestDto(null);
        }
        specialistApprovalService.approveSpecialist(specialistId, decisionDto);
    }

    @PostMapping("/{specialistId}/reject")
    @Operation(summary = "Reject a specialist application")
    public void rejectSpecialist(
            @PathVariable Long specialistId,
            @RequestBody(required = false) SpecialistDecisionRequestDto decisionDto
    ) {
        if (decisionDto == null) {
            decisionDto = new SpecialistDecisionRequestDto(null);
        }
        specialistApprovalService.rejectSpecialist(specialistId, decisionDto);
    }
}
