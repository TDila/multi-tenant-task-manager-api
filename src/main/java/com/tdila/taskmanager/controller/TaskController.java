package com.tdila.taskmanager.controller;

import com.tdila.taskmanager.dto.TaskCreateRequestDTO;
import com.tdila.taskmanager.dto.TaskResponseDTO;
import com.tdila.taskmanager.dto.TaskStatusUpdateRequestDTO;
import com.tdila.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getUserTasks(){
        List<TaskResponseDTO> tasks = taskService.getAllTasksForCurrentUser();
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{taskId}/status")
    public ResponseEntity<TaskResponseDTO> updateTaskStatus(
            @PathVariable String taskId,
            @RequestBody TaskStatusUpdateRequestDTO request
            ){
        TaskResponseDTO response = taskService.updateTaskStatus(taskId, request);
        return ResponseEntity.ok(response);
    }
}
