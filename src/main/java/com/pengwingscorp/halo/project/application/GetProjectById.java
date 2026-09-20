package com.pengwingscorp.halo.project.application;

import com.pengwingscorp.halo.project.exception.ProjectNotFound;
import com.pengwingscorp.halo.project.infrastructure.ProjectRepository;
import com.pengwingscorp.halo.project.domain.Project;
import com.pengwingscorp.halo.project.web.dto.GetProjectRequest;
import com.pengwingscorp.halo.project.web.dto.ProjectResponse;
import org.springframework.stereotype.Service;

@Service
public class GetProjectById {
    private final ProjectRepository projectRepository;

    public GetProjectById(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ProjectResponse execute(GetProjectRequest getProjectRequest) {
        Project project = this.projectRepository.findById(getProjectRequest.id());
        if (project == null) {
            throw new ProjectNotFound(getProjectRequest.id().toString());
        }
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreateAt()
        );
    }
}
