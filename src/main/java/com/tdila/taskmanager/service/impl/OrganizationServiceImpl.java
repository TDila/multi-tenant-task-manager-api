package com.tdila.taskmanager.service.impl;

import com.tdila.taskmanager.dto.OrganizationCreateRequestDTO;
import com.tdila.taskmanager.dto.OrganizationResponseDTO;
import com.tdila.taskmanager.entity.Organization;
import com.tdila.taskmanager.entity.User;
import com.tdila.taskmanager.repository.OrganizationRepository;
import com.tdila.taskmanager.repository.UserRepository;
import com.tdila.taskmanager.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    @Override
    public OrganizationResponseDTO createOrganization(OrganizationCreateRequestDTO request) {
        organizationRepository.findByName(request.getName()).ifPresent(org -> {
            throw new RuntimeException("Organization already exists");
        });

        Organization organization = Organization.builder()
                .name(request.getName())
                .build();
        organizationRepository.save(organization);

        String email = getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setOrganization(organization);
        userRepository.save(user);

        return OrganizationResponseDTO.builder()
                .id(organization.getId())
                .name(organization.getName())
                .build();
    }

    private String getCurrentUserEmail(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails userDetails){
            return userDetails.getUsername();
        }
        return principal.toString();
    }
}
