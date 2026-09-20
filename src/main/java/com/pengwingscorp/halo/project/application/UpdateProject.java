package com.pengwingscorp.halo.project.application;

import com.pengwingscorp.halo.project.exception.ProjectNotFound;
import com.pengwingscorp.halo.project.infrastructure.ProjectRepository;
import com.pengwingscorp.halo.project.domain.Project;
import com.pengwingscorp.halo.project.web.dto.ProjectRequest;
import com.pengwingscorp.halo.project.web.dto.ProjectResponse;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

// NOT TESTED
@Service
public class UpdateProject {
    private final ProjectRepository projectRepository;

    public UpdateProject(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ProjectResponse execute(UUID id, ProjectRequest projectRequest) {
        Project project = new Project(
                id,
                projectRequest.name(),
                projectRequest.description(),
                new Date());
        Project updatedProject = this.projectRepository.update(project);
        if (updatedProject == null) {
            throw new ProjectNotFound(id.toString());
        }
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreateAt()
        );
    }

}
