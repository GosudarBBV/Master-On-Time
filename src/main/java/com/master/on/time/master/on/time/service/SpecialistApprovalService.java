package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.SpecialistApplicationDto;
import com.master.on.time.master.on.time.dto.SpecialistDecisionRequestDto;
import java.util.List;

public interface SpecialistApprovalService {

    List<SpecialistApplicationDto> getPendingApplications();

    SpecialistApplicationDto getApplicationDetails(Long specialistId);

    void approveSpecialist(Long specialistId, SpecialistDecisionRequestDto decisionDto);

    void rejectSpecialist(Long specialistId, SpecialistDecisionRequestDto decisionDto);
}
