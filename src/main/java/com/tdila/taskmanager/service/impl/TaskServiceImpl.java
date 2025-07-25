package com.tdila.taskmanager.service.impl;

import com.tdila.taskmanager.dto.*;
import com.tdila.taskmanager.entity.Task;
import com.tdila.taskmanager.entity.TaskStatus;
import com.tdila.taskmanager.entity.User;
import com.tdila.taskmanager.repository.TaskRepository;
import com.tdila.taskmanager.repository.UserRepository;
import com.tdila.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public TaskResponseDTO createTask(TaskCreateRequestDTO request) {
        String creatorEmail = getCurrentUserEmail();
        User creator = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new RuntimeException("Creator not found"));

        Set<User> collaborators = new HashSet<>();
        if(request.getCollaboratorIds() != null){
            collaborators = request.getCollaboratorIds().stream()
                    .map(id -> userRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Collaborator not found: "+id)))
                    .collect(Collectors.toSet());
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(TaskStatus.TODO)
                .creator(creator)
                .organization(creator.getOrganization())
                .collaborators(collaborators)
                .build();

        Task savedTask = taskRepository.save(task);

        return TaskResponseDTO.builder()
                .id(savedTask.getId())
                .title(savedTask.getTitle())
                .description(savedTask.getDescription())
                .status(savedTask.getStatus())
                .creatorId(savedTask.getCreator().getId())
                .organizationId(savedTask.getOrganization().getId())
                .collaboratorIds(savedTask.getCollaborators().stream()
                        .map(User::getId)
                        .collect(Collectors.toList()))
                .build();
    }

    private String getCurrentUserEmail(){
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        if(principal instanceof UserDetails userDetails){
            return userDetails.getUsername();
        }
        return principal.toString();
    }

    @Override
    public List<TaskResponseDTO> getAllTasksForCurrentUser() {
        String userEmail = getCurrentUserEmail();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Task> createdTasks = taskRepository.findByCreator(user);

        List<Task> assignedTasks = taskRepository.findByCollaboratorsContaining(user);

        Set<Task> allTasks = new HashSet<>();
        allTasks.addAll(createdTasks);
        allTasks.addAll(assignedTasks);

        return allTasks.stream().map(task -> TaskResponseDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .creatorId(task.getCreator().getId())
                .organizationId(task.getOrganization().getId())
                .collaboratorIds(task.getCollaborators().stream()
                        .map(User::getId)
                        .collect(Collectors.toList()))
                .build()).toList();
    }

    @Override
    @Transactional
    public TaskResponseDTO updateTaskStatus(String taskId, TaskStatusUpdateRequestDTO request) {
        UUID id = UUID.fromString(taskId);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        String currentUserEmail = getCurrentUserEmail();
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!task.getCreator().equals(user) && !task.getCollaborators().contains(user)){
            throw new RuntimeException("You are not allowed to update this task");
        }

        task.setStatus(request.getStatus());
        taskRepository.save(task);

        return TaskResponseDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .creatorId(task.getCreator().getId())
                .organizationId(task.getOrganization().getId())
                .collaboratorIds(task.getCollaborators().stream()
                        .map(User::getId)
                        .toList())
                .build();
    }

    @Override
    @Transactional
    public TaskResponseDTO updateTaskCollaborators(String taskId, TaskCollaboratorUpdateDTO request) {
        UUID id = UUID.fromString(taskId);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        String currentEmail = getCurrentUserEmail();
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!task.getCreator().equals(user)){
            throw new RuntimeException("Only the creator can update collaborators");
        }

        Set<User> collaborators = request.getCollaboratorIds().stream()
                .map(uuid -> userRepository.findById(uuid)
                        .orElseThrow(() -> new RuntimeException("User not found: "+uuid)))
                .collect(Collectors.toSet());

        task.setCollaborators(collaborators);
        taskRepository.save(task);

        return TaskResponseDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .creatorId(task.getCreator().getId())
                .organizationId(task.getOrganization().getId())
                .collaboratorIds(task.getCollaborators().stream()
                        .map(User::getId)
                        .toList())
                .build();
    }

    @Override
    @Transactional
    public TaskResponseDTO updateTaskDetails(String taskId, TaskUpdateRequestDTO request) {
        UUID id = UUID.fromString(taskId);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        String currentEmail = getCurrentUserEmail();
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!task.getCreator().equals(user)){
            throw new RuntimeException("Only the creator can update the task details");
        }

        if(request.getTitle() != null && !request.getTitle().isBlank()){
            task.setTitle(request.getTitle());
        }

        if(request.getDescription() != null){
            task.setDescription(request.getDescription());
        }

        taskRepository.save(task);

        return TaskResponseDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .creatorId(task.getCreator().getId())
                .organizationId(task.getOrganization().getId())
                .collaboratorIds(task.getCollaborators().stream()
                        .map(User::getId)
                        .toList())
                .build();
    }

    @Override
    public void deleteTask(String taskId) {
        UUID id = UUID.fromString(taskId);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        String currentEmail = getCurrentUserEmail();
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!task.getCreator().equals(user)){
            throw new RuntimeException("Only the creator can delete this task");
        }

        taskRepository.delete(task);
    }
}
