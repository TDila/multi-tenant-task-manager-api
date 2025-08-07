package com.tdila.taskmanager.dto;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskCollaboratorRemoveDTO {
    private Set<UUID> collaboratorIds;
}
