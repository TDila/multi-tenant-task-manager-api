package com.tdila.taskmanager.controller;

import com.tdila.taskmanager.dto.OrganizationCreateRequestDTO;
import com.tdila.taskmanager.dto.OrganizationResponseDTO;
import com.tdila.taskmanager.repository.OrganizationRepository;
import com.tdila.taskmanager.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/organization")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;

    @PostMapping("/create")
    public ResponseEntity<OrganizationResponseDTO> createOrganization(
            @RequestBody OrganizationCreateRequestDTO request){
        OrganizationResponseDTO response = organizationService.createOrganization(request);
        return ResponseEntity.ok(response);
    }
}
