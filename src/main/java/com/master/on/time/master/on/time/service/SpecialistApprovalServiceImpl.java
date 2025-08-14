package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.SpecialistApplicationDto;
import com.master.on.time.master.on.time.dto.SpecialistDecisionRequestDto;
import com.master.on.time.master.on.time.exception.EntityNotFoundException;
import com.master.on.time.master.on.time.mapper.SpecialistMapper;
import com.master.on.time.master.on.time.model.Specialist;
import com.master.on.time.master.on.time.repository.SpecialistRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecialistApprovalServiceImpl implements SpecialistApprovalService {
    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_ACTIVE = "ACTIVE";
    private static final String STATUS_REJECTED = "REJECTED";

    private final SpecialistRepository specialistRepository;
    private final SpecialistMapper specialistMapper;

    @Override
    public List<SpecialistApplicationDto> getPendingApplications() {
        return specialistRepository.findByStatus(STATUS_PENDING)
                .stream()
                .map(specialistMapper::toApplicationDto)
                .toList();
    }

    @Override
    public SpecialistApplicationDto getApplicationDetails(Long specialistId) {
        Specialist specialist = findSpecialistById(specialistId);
        return specialistMapper.toApplicationDto(specialist);
    }

    @Override
    public void approveSpecialist(Long specialistId, SpecialistDecisionRequestDto decisionDto) {
        Specialist specialist = findSpecialistById(specialistId);
        if (!STATUS_PENDING.equals(specialist.getStatus())) {
            throw new IllegalStateException("Only pending specialists can be approved");
        }
        specialist.setStatus(STATUS_ACTIVE);
        specialist.setAdminNotes(decisionDto.notes());
        specialistRepository.save(specialist);
    }

    @Override
    public void rejectSpecialist(Long specialistId, SpecialistDecisionRequestDto decisionDto) {
        Specialist specialist = findSpecialistById(specialistId);
        if (!STATUS_PENDING.equals(specialist.getStatus())) {
            throw new IllegalStateException("Only pending specialists can be rejected");
        }
        specialist.setStatus(STATUS_REJECTED);
        specialist.setAdminNotes(decisionDto.notes());
        specialistRepository.save(specialist);
    }

    private Specialist findSpecialistById(Long id) {
        return specialistRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Specialist not found"));
    }
}
