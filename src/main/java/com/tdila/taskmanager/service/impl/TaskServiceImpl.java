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

import java.util.*;
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

        if (creator.getTenant() == null) {
            throw new RuntimeException("You must join a tenant before creating tasks.");
        }

        Set<User> collaborators = new HashSet<>();
        if (request.getCollaboratorIds() != null) {
            collaborators = request.getCollaboratorIds().stream()
                    .map(id -> userRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Collaborator not found: " + id)))
                    .peek(user -> {
                        if (user.getTenant() == null || !user.getTenant().getId().equals(creator.getTenant().getId())) {
                            throw new RuntimeException("Collaborator must belong to the same tenant.");
                        }
                    })
                    .collect(Collectors.toSet());
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(TaskStatus.TODO)
                .creator(creator)
                .tenant(creator.getTenant())
                .collaborators(collaborators)
                .build();

        Task savedTask = taskRepository.save(task);

        return TaskResponseDTO.builder()
                .id(savedTask.getId())
                .title(savedTask.getTitle())
                .description(savedTask.getDescription())
                .status(savedTask.getStatus())
                .creatorId(savedTask.getCreator().getId())
                .tenantId(savedTask.getTenant().getId())
                .collaboratorIds(savedTask.getCollaborators().stream()
                        .map(User::getId)
                        .collect(Collectors.toSet()))
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
    @Transactional
    public List<TaskResponseDTO> getAllTasksForCurrentUser() {
        String userEmail = getCurrentUserEmail();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Task> createdTasks = taskRepository.findByCreator(user);

        List<Task> assignedTasks = taskRepository.findByCollaboratorsContaining(user);

        Set<Task> allTasks = new HashSet<>();
        allTasks.addAll(createdTasks);
        allTasks.addAll(assignedTasks);
        System.out.println("add tasks: "+allTasks);
        return allTasks.stream().map(task -> TaskResponseDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .creatorId(task.getCreator().getId())
                .tenantId(task.getTenant().getId())
                .collaboratorIds(task.getCollaborators().stream()
                        .map(User::getId)
                        .collect(Collectors.toSet()))
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

        if (!task.getCreator().equals(user)) {
            throw new RuntimeException("Only the task creator can update the task status.");
        }

        task.setStatus(request.getStatus());
        taskRepository.save(task);

        return TaskResponseDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .creatorId(task.getCreator().getId())
                .tenantId(task.getTenant().getId())
                .collaboratorIds(task.getCollaborators().stream()
                        .map(User::getId)
                        .collect(Collectors.toSet()))
                .build();
    }

    @Override
    @Transactional
    public TaskResponseDTO addCollaborators(String taskId, TaskCollaboratorAddDTO request) {
        UUID id = UUID.fromString(taskId);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User currentUser = userRepository.findByEmail(getCurrentUserEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!task.getCreator().equals(currentUser)) {
            throw new RuntimeException("Only the creator can add collaborators");
        }

        UUID currentTenantId = currentUser.getTenant() != null ? currentUser.getTenant().getId() : null;
        if (currentTenantId == null) {
            throw new RuntimeException("Current user is not assigned to any tenant");
        }

        Set<User> newCollaborators = request.getCollaboratorIds().stream()
                .map(uuid -> userRepository.findById(uuid)
                        .orElseThrow(() -> new RuntimeException("User not found: " + uuid)))
                .peek(user -> {
                    if (user.getTenant() == null || !user.getTenant().getId().equals(currentTenantId)) {
                        throw new RuntimeException("User " + user.getEmail() + " must belong to the same tenant");
                    }
                })
                .collect(Collectors.toSet());

        task.getCollaborators().addAll(newCollaborators);
        taskRepository.save(task);

        return toTaskResponseDTO(task);
    }

    @Override
    @Transactional
    public TaskResponseDTO removeCollaborators(String taskId, TaskCollaboratorRemoveDTO request) {
        UUID id = UUID.fromString(taskId);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User currentUser = userRepository.findByEmail(getCurrentUserEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!task.getCreator().equals(currentUser)) {
            throw new RuntimeException("Only the creator can remove collaborators");
        }

        Set<User> toRemove = request.getCollaboratorIds().stream()
                .map(uuid -> userRepository.findById(uuid)
                        .orElseThrow(() -> new RuntimeException("User not found: " + uuid)))
                .collect(Collectors.toSet());

        task.getCollaborators().removeAll(toRemove);
        taskRepository.save(task);

        return toTaskResponseDTO(task);
    }

    private TaskResponseDTO toTaskResponseDTO(Task task) {
        return TaskResponseDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .creatorId(task.getCreator().getId())
                .tenantId(task.getTenant().getId()) // update to tenantId if not already
                .collaboratorIds(task.getCollaborators().stream()
                        .map(User::getId)
                        .collect(Collectors.toSet()))
                .build();
    }

    private TaskResponseDTO findById(UUID taskId){
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isPresent()){
            return toTaskResponseDTO(task.get());
        }else{
            throw new RuntimeException("Task not found with id: "+taskId);
        }
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

        if (!task.getCreator().getId().equals(user.getId())) {
            throw new RuntimeException("Only the creator can update task details.");
        }

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            task.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }

        Task updatedTask = taskRepository.save(task);

        return toTaskResponseDTO(updatedTask);
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
