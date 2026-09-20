package com.pengwingscorp.halo.task.infrastructure.persistence;

import com.pengwingscorp.halo.project.infrastructure.persistence.ProjectJpaEntity;
import com.pengwingscorp.halo.task.domain.EnumTaskStatus;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "tasks")
public class TaskJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String status;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectJpaEntity project;
    @Column(nullable = false)
    private Date createAt;
}
