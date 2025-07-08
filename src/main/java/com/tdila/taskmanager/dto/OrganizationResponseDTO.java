package com.tdila.taskmanager.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationResponseDTO {
    private UUID id;
    private String name;
}
