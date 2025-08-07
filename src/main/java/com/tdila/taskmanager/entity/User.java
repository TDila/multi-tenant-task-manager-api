package com.tdila.taskmanager.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"tenant", "createdTasks", "collaboratingTasks"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role; // SYSTEM_USER, TENANT_ADMIN

    @ManyToOne
    private Tenant tenant;

    @OneToMany(mappedBy = "creator")
    private List<Task> createdTasks;

    @ManyToMany(mappedBy = "collaborators")
    private Set<Task> collaboratingTasks;
}
