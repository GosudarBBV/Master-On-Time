package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.AvailabilityRequestDto;
import com.master.on.time.master.on.time.dto.UnavailabilityRequestDto;
import java.util.List;

public interface ScheduleService {

    void setAvailability(Long specialistId, List<AvailabilityRequestDto> availabilityList);

    void addUnavailability(Long specialistId, UnavailabilityRequestDto dto);

    List<AvailabilityRequestDto> getAvailability(Long specialistId);

    List<UnavailabilityRequestDto> getUnavailabilities(Long specialistId);
}
