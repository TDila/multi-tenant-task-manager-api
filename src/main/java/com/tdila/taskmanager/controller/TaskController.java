package com.tdila.taskmanager.controller;

import com.tdila.taskmanager.dto.TaskCreateRequestDTO;
import com.tdila.taskmanager.dto.TaskResponseDTO;
import com.tdila.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskCreateRequestDTO request){
        TaskResponseDTO response = taskService.createTask(request);
        return ResponseEntity.ok(response);
    }
}
