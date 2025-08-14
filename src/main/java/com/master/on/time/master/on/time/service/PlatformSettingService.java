package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.PlatformSettingDto;
import java.util.List;

public interface PlatformSettingService {
    List<PlatformSettingDto> getAllSettings();

    PlatformSettingDto getSettingByKey(String keyName);

    PlatformSettingDto updateSetting(Long id, PlatformSettingDto dto);
}
