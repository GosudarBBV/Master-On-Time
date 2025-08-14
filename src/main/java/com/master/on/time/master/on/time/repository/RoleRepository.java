package com.master.on.time.master.on.time.repository;

import com.master.on.time.master.on.time.model.Role;
import com.master.on.time.master.on.time.model.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
