package com.tdila.taskmanager.controller;

import com.tdila.taskmanager.dto.AddUserToTenantRequest;
import com.tdila.taskmanager.dto.TenantCreationRequest;
import com.tdila.taskmanager.dto.TenantResponseDTO;
import com.tdila.taskmanager.entity.Tenant;
import com.tdila.taskmanager.security.CustomUserDetails;
import com.tdila.taskmanager.security.CustomUserDetailsService;
import com.tdila.taskmanager.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantController {
    private final TenantService tenantService;

    @PostMapping
    public ResponseEntity<Tenant> createTenant(@RequestBody TenantCreationRequest request) {
        Tenant tenant = tenantService.createTenant(request.getName(), request.getAdminId());
        return ResponseEntity.status(HttpStatus.CREATED).body(tenant);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TenantResponseDTO> getTenantById(@PathVariable UUID id) {
        return ResponseEntity.ok(tenantService.findById(id));
    }

    @PostMapping("/add-user")
    public ResponseEntity<String> addUserToTenant(
            @RequestBody AddUserToTenantRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID adminId = ((CustomUserDetails) userDetails).getUser().getId();
        tenantService.addUserToTenant(request.email(), adminId);
        return ResponseEntity.ok("User added to tenant successfully");
    }
}
