package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.AvailabilityRequestDto;
import com.master.on.time.master.on.time.dto.UnavailabilityRequestDto;
import com.master.on.time.master.on.time.exception.EntityNotFoundException;
import com.master.on.time.master.on.time.mapper.ScheduleMapper;
import com.master.on.time.master.on.time.model.Availability;
import com.master.on.time.master.on.time.model.Unavailability;
import com.master.on.time.master.on.time.model.User;
import com.master.on.time.master.on.time.repository.AvailabilityRepository;
import com.master.on.time.master.on.time.repository.UnavailabilityRepository;
import com.master.on.time.master.on.time.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleServiceImpl implements ScheduleService {

    private final AvailabilityRepository availabilityRepository;
    private final UnavailabilityRepository unavailabilityRepository;
    private final UserRepository userRepository;
    private final ScheduleMapper scheduleMapper;

    private User getSpecialistOrThrow(Long specialistId) {
        return userRepository.findById(specialistId)
                .orElseThrow(() -> new EntityNotFoundException("Specialist not found"));
    }

    @Override
    public void setAvailability(Long specialistId, List<AvailabilityRequestDto> availabilityList) {
        User specialist = getSpecialistOrThrow(specialistId);

        availabilityRepository.deleteBySpecialistId(specialistId);

        List<Availability> newAvailabilities = availabilityList.stream()
                .map(dto -> {
                    Availability availability = scheduleMapper.toAvailabilityEntity(dto);
                    availability.setSpecialist(specialist);
                    return availability;
                })
                .toList();

        availabilityRepository.saveAll(newAvailabilities);
    }

    @Override
    public void addUnavailability(Long specialistId, UnavailabilityRequestDto dto) {
        User specialist = getSpecialistOrThrow(specialistId);

        Unavailability unavailability = scheduleMapper.toUnavailabilityEntity(dto);
        unavailability.setSpecialist(specialist);

        unavailabilityRepository.save(unavailability);
    }

    @Override
    public List<AvailabilityRequestDto> getAvailability(Long specialistId) {
        getSpecialistOrThrow(specialistId);
        return availabilityRepository.findBySpecialistId(specialistId).stream()
                .map(scheduleMapper::toAvailabilityDto)
                .toList();
    }

    @Override
    public List<UnavailabilityRequestDto> getUnavailabilities(Long specialistId) {
        getSpecialistOrThrow(specialistId);
        return unavailabilityRepository.findBySpecialistId(specialistId).stream()
                .map(scheduleMapper::toUnavailabilityDto)
                .toList();
    }
}
