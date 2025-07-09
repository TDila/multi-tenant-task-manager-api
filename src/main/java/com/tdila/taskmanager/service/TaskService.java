package com.tdila.taskmanager.service;

import com.tdila.taskmanager.dto.TaskCreateRequestDTO;
import com.tdila.taskmanager.dto.TaskResponseDTO;

public interface TaskService {
    TaskResponseDTO createTask(TaskCreateRequestDTO request);
}
