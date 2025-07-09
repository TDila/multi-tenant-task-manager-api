package com.tdila.taskmanager.dto;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskCreateRequestDTO {
    private String title;
    private String description;
    private Set<UUID> collaboratorIds;
}
