package com.pengwingscorp.halo.project.infrastructure.persistence;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "projects")
public class ProjectJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Date createAt;

//    @OneToMany(mappedBy = "project")
//    private Set<TaskJpaEntity> tasks = new HashSet<>();

    public ProjectJpaEntity() {
    }

    public ProjectJpaEntity(UUID id, String name, String description, Date createAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createAt = createAt;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreateAt() {
        return createAt;
    }

}
