package com.pengwingscorp.halo.project.application;

import com.pengwingscorp.halo.project.exception.ProjectNotFound;
import com.pengwingscorp.halo.project.infrastructure.ProjectRepository;
import com.pengwingscorp.halo.project.infrastructure.persistence.ProjectJpaEntity;
import com.pengwingscorp.halo.project.web.dto.ProjectResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetProjectById {
    private final ProjectRepository projectRepository;

    public GetProjectById(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ProjectResponse execute(UUID id) {
        ProjectJpaEntity projectJpaEntity = this.projectRepository
                .findById(id)
                .orElseThrow(() -> new ProjectNotFound(id.toString()));
        
        return new ProjectResponse(
                projectJpaEntity.getId(),
                projectJpaEntity.getName(),
                projectJpaEntity.getDescription(),
                projectJpaEntity.getCreateAt()
        );
    }
}
