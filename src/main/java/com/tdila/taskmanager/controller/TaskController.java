package com.tdila.taskmanager.controller;

import com.tdila.taskmanager.dto.*;
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

    @PutMapping("/{taskId}/collaborators")
    public ResponseEntity<TaskResponseDTO> updateTaskCollaborators(
            @PathVariable String taskId,
            @RequestBody TaskCollaboratorUpdateDTO request
            ){
        TaskResponseDTO response = taskService.updateTaskCollaborators(taskId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> updateTaskDetails(
            @PathVariable String taskId,
            @RequestBody TaskUpdateRequestDTO request
            ){
        TaskResponseDTO response = taskService.updateTaskDetails(taskId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable String taskId){
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
