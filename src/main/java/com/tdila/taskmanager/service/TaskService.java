package com.tdila.taskmanager.service;

import com.tdila.taskmanager.dto.TaskCreateRequestDTO;
import com.tdila.taskmanager.dto.TaskResponseDTO;
import com.tdila.taskmanager.dto.TaskStatusUpdateRequestDTO;

import java.util.List;

public interface TaskService {
    TaskResponseDTO createTask(TaskCreateRequestDTO request);
    List<TaskResponseDTO> getAllTasksForCurrentUser();
    TaskResponseDTO updateTaskStatus(String taskId, TaskStatusUpdateRequestDTO request);
}
