package com.tdila.taskmanager.service;

import com.tdila.taskmanager.dto.TenantResponseDTO;
import com.tdila.taskmanager.entity.Tenant;
import com.tdila.taskmanager.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface TenantService {
    Tenant createTenant(String tenantName, UUID adminUserId);
    TenantResponseDTO findById(UUID tenantId);
    Optional<Tenant> findByName(String name);
    void addUserToTenant(String userEmail, UUID adminId);

}
