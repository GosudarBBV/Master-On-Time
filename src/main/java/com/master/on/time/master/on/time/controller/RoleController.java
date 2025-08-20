package com.master.on.time.master.on.time.controller;

import com.master.on.time.master.on.time.model.Role;
import com.master.on.time.master.on.time.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleRepository roleRepository;

    @PostMapping
    public Role createRole(@RequestBody Role role) {
        return roleRepository.save(role);
    }
}
