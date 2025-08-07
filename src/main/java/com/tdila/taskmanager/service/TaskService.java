package com.tdila.taskmanager.service;

import com.tdila.taskmanager.dto.*;

import java.util.List;

public interface TaskService {
    TaskResponseDTO createTask(TaskCreateRequestDTO request);
    List<TaskResponseDTO> getAllTasksForCurrentUser();
    TaskResponseDTO updateTaskStatus(String taskId, TaskStatusUpdateRequestDTO request);
    TaskResponseDTO updateTaskDetails(String taskId, TaskUpdateRequestDTO request);
    void deleteTask(String taskId);
    TaskResponseDTO addCollaborators(String taskId, TaskCollaboratorAddDTO request);
    TaskResponseDTO removeCollaborators(String taskId, TaskCollaboratorRemoveDTO request);
}
