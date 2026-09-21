package com.pengwingscorp.halo.project.application;

import com.pengwingscorp.halo.project.exception.ProjectNotFound;
import com.pengwingscorp.halo.project.infrastructure.ProjectRepository;
import com.pengwingscorp.halo.project.domain.Project;
import com.pengwingscorp.halo.project.infrastructure.persistence.ProjectJpaEntity;
import com.pengwingscorp.halo.project.infrastructure.persistence.ProjectJpaMapper;
import com.pengwingscorp.halo.project.web.dto.ProjectRequest;
import com.pengwingscorp.halo.project.web.dto.ProjectResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateProject {
    private final ProjectRepository projectRepository;

    public UpdateProject(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
    @Transactional
    public ProjectResponse execute(UUID id, ProjectRequest projectRequest) {
        ProjectJpaEntity projectJpaEntity = this.projectRepository
                .findById(id)
                .orElseThrow(() -> new ProjectNotFound(id.toString()));
        Project project = ProjectJpaMapper.toDomain(projectJpaEntity);
        project.update(projectRequest.name(), projectRequest.description());
        ProjectJpaEntity updatedProjectJpaEntity = this.projectRepository.save(ProjectJpaMapper.toDatabase(project));
        return new ProjectResponse(
                updatedProjectJpaEntity.getId(),
                updatedProjectJpaEntity.getName(),
                updatedProjectJpaEntity.getDescription(),
                updatedProjectJpaEntity.getCreateAt()
        );
    }

}
