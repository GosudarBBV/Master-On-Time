package com.master.on.time.master.on.time.controller;

import com.master.on.time.master.on.time.dto.PlatformSettingDto;
import com.master.on.time.master.on.time.service.PlatformSettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/settings")
@RequiredArgsConstructor
@Tag(name = "Platform Settings", description = "Manage platform-wide settings")
public class PlatformSettingController {

    private final PlatformSettingService settingService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all platform settings")
    @GetMapping
    public List<PlatformSettingDto> getAllSettings() {
        return settingService.getAllSettings();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get platform setting by key name")
    @GetMapping("/{keyName}")
    public PlatformSettingDto getSetting(@PathVariable String keyName) {
        return settingService.getSettingByKey(keyName);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update platform setting")
    @PutMapping("/{id}")
    public PlatformSettingDto updateSetting(@PathVariable Long id,
                                            @RequestBody PlatformSettingDto dto) {
        return settingService.updateSetting(id, dto);
    }
}
