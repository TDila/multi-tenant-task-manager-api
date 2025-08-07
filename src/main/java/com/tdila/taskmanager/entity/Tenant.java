package com.tdila.taskmanager.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "tenants")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"admin", "users", "tasks"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Tenant {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    private LocalDateTime createdAt;

    @OneToOne
    private User admin;

    @OneToMany(mappedBy = "tenant")
    private List<User> users;

    @OneToMany(mappedBy = "tenant")
    private List<Task> tasks;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}

