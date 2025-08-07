package com.tdila.taskmanager.service.impl;

import com.tdila.taskmanager.dto.TenantResponseDTO;
import com.tdila.taskmanager.entity.Task;
import com.tdila.taskmanager.entity.Tenant;
import com.tdila.taskmanager.entity.User;
import com.tdila.taskmanager.repository.TenantRepository;
import com.tdila.taskmanager.repository.UserRepository;
import com.tdila.taskmanager.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {
    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;

    @Override
    public Tenant createTenant(String tenantName, UUID adminUserId) {
        User admin = userRepository.findById(adminUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (admin.getTenant() != null){
            throw new RuntimeException("User already belongs to a tenant");
        }

        Tenant tenant = Tenant.builder()
                .name(tenantName)
                .admin(admin)
                .build();

        tenant = tenantRepository.save(tenant);

        admin.setTenant(tenant);
        userRepository.save(admin);

        return null;
    }

    @Override
    public TenantResponseDTO findById(UUID tenantId) {
        Optional<Tenant> tenantResult = tenantRepository.findById(tenantId);
        if (tenantResult.isPresent()){
            return toTenantResponseDTO(tenantResult.get());
        }else{
            throw new RuntimeException("Not found");
        }
    }

    public TenantResponseDTO toTenantResponseDTO(Tenant tenant){
        return TenantResponseDTO.builder()
                .id(tenant.getId())
                .name(tenant.getName())
                .createdAt(tenant.getCreatedAt())
                .adminId(tenant.getAdmin().getId())
                .userIds(tenant.getUsers().stream()
                        .map(User::getId)
                        .collect(Collectors.toSet()))
                .taskIds(tenant.getTasks().stream()
                        .map(Task::getId)
                        .collect(Collectors.toSet()))
                .build();
    }

    @Override
    public Optional<Tenant> findByName(String name) {
        return tenantRepository.findByName(name);
    }

    @Override
    public void addUserToTenant(String userEmail, UUID adminId) {
        User adminUser = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin user not found"));

        Tenant tenant = tenantRepository.findByAdmin(adminUser)
                .orElseThrow(() -> new RuntimeException("Tenant not found for admin"));

        User userToAdd = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User to add not found"));

        userToAdd.setTenant(tenant); // assuming User has a @ManyToOne Tenant field
        userRepository.save(userToAdd);
    }
}
