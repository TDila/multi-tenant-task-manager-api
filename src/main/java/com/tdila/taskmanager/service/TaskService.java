package com.tdila.taskmanager.service;

import com.tdila.taskmanager.dto.*;

import java.util.List;

public interface TaskService {
    TaskResponseDTO createTask(TaskCreateRequestDTO request);
    List<TaskResponseDTO> getAllTasksForCurrentUser();
    TaskResponseDTO updateTaskStatus(String taskId, TaskStatusUpdateRequestDTO request);
    TaskResponseDTO updateTaskCollaborators(String taskId, TaskCollaboratorUpdateDTO request);
    TaskResponseDTO updateTaskDetails(String taskId, TaskUpdateRequestDTO request);
    void deleteTask(String taskId);
}
