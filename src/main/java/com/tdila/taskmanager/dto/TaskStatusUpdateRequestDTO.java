package com.tdila.taskmanager.dto;

import com.tdila.taskmanager.entity.TaskStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskStatusUpdateRequestDTO {
    private TaskStatus status;
}
