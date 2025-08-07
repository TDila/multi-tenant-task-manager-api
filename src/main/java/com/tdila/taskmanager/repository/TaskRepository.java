package com.tdila.taskmanager.repository;

import com.tdila.taskmanager.entity.Task;
import com.tdila.taskmanager.entity.TaskStatus;
import com.tdila.taskmanager.entity.Tenant;
import com.tdila.taskmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByTenant(Tenant tenant);
    List<Task> findByCreator(User creator);
    List<Task> findByCollaboratorsContaining(User user);
    List<Task> findByStatusAndTenant(TaskStatus status, Tenant tenant);
}
