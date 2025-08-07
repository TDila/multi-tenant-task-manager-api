package com.tdila.taskmanager.dto;

import com.tdila.taskmanager.entity.TaskStatus;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskResponseDTO {
    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private UUID creatorId;
    private UUID tenantId;
    private Set<UUID> collaboratorIds;
}
