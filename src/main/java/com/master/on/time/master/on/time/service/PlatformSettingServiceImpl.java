package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.PlatformSettingDto;
import com.master.on.time.master.on.time.mapper.PlatformSettingMapper;
import com.master.on.time.master.on.time.model.PlatformSetting;
import com.master.on.time.master.on.time.repository.PlatformSettingRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlatformSettingServiceImpl implements PlatformSettingService {

    private final PlatformSettingRepository settingRepository;
    private final PlatformSettingMapper settingMapper;

    @Override
    public List<PlatformSettingDto> getAllSettings() {
        return settingMapper.toDtoList(settingRepository.findAll());
    }

    @Override
    public PlatformSettingDto getSettingByKey(String keyName) {
        return settingMapper.toDto(
                settingRepository.findByKeyName(keyName)
                        .orElseThrow(()
                                -> new EntityNotFoundException("Setting not found with key: "
                                + keyName))
        );
    }

    @Override
    public PlatformSettingDto updateSetting(Long id, PlatformSettingDto dto) {
        PlatformSetting setting = findSettingById(id);
        setting.setSettingValue(dto.value());
        return settingMapper.toDto(settingRepository.save(setting));
    }

    private PlatformSetting findSettingById(Long id) {
        return settingRepository.findById(id)
                .orElseThrow(()
                        -> new EntityNotFoundException("Setting not found with id: " + id));
    }
}
