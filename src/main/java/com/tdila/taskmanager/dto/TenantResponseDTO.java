package com.tdila.taskmanager.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TenantResponseDTO {
    private UUID id;
    private String name;
    private LocalDateTime createdAt;
    private UUID adminId;
    private Set<UUID> userIds;
    private Set<UUID> taskIds;
}
