package com.tdila.taskmanager.service;

import com.tdila.taskmanager.dto.OrganizationCreateRequestDTO;
import com.tdila.taskmanager.dto.OrganizationResponseDTO;

public interface OrganizationService {
    OrganizationResponseDTO createOrganization(OrganizationCreateRequestDTO request);
}
