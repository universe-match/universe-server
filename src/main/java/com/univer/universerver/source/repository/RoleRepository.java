package com.univer.universerver.source.repository;

import com.univer.universerver.source.model.Role;
import com.univer.universerver.source.utils.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(RoleName roleName);
}
