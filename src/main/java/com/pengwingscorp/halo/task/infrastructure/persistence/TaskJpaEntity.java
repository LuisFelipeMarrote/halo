package com.pengwingscorp.halo.task.infrastructure.persistence;

import com.pengwingscorp.halo.project.infrastructure.persistence.ProjectJpaEntity;
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
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectJpaEntity project;
    @Column(nullable = false)
    private Date createAt;

    public TaskJpaEntity(UUID id, String title, String description, String status, ProjectJpaEntity project, Date createAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.project = project;
        this.createAt = createAt;
    }

    public TaskJpaEntity() {
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public ProjectJpaEntity getProject() {
        return project;
    }

    public Date getCreateAt() {
        return createAt;
    }
}
