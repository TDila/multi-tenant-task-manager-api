package com.tdila.taskmanager.repository;

import com.tdila.taskmanager.entity.Tenant;
import com.tdila.taskmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends JpaRepository<Tenant, UUID> {
    Optional<Tenant> findByName(String name);
    Optional<Tenant> findByAdmin(User admin);
}
