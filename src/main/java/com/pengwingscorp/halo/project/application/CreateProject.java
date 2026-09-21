package com.pengwingscorp.halo.project.application;

import com.pengwingscorp.halo.project.infrastructure.ProjectRepository;
import com.pengwingscorp.halo.project.domain.Project;
import com.pengwingscorp.halo.project.infrastructure.persistence.ProjectJpaEntity;
import com.pengwingscorp.halo.project.infrastructure.persistence.ProjectJpaMapper;
import com.pengwingscorp.halo.project.web.dto.ProjectRequest;
import com.pengwingscorp.halo.project.web.dto.ProjectResponse;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CreateProject {
    private final ProjectRepository projectRepository;

    public CreateProject(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ProjectResponse execute(ProjectRequest projectRequest) {
        Project newProject = new Project.Builder()
                .setNome(projectRequest.description())
                .setDescription(projectRequest.description())
                .setCreateAt(new Date())
                .build();
        ProjectJpaEntity projectJpaEntity = projectRepository.save(ProjectJpaMapper.toDatabase(newProject));
        return new ProjectResponse(
                projectJpaEntity.getId(),
                projectJpaEntity.getName(),
                projectJpaEntity.getDescription(),
                projectJpaEntity.getCreateAt()
        );
    }
}
