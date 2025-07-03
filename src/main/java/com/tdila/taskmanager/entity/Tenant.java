package com.tdila.taskmanager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tenants")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tenant {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String organizationName;

    private LocalDateTime createdAt;

    @PrePersist
    public void setCreationTimestamp(){
        this.createdAt = LocalDateTime.now();
    }
}
