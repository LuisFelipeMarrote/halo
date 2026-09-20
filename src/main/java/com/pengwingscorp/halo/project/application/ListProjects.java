package com.pengwingscorp.halo.project.application;

import com.pengwingscorp.halo.project.infrastructure.ProjectRepository;
import com.pengwingscorp.halo.project.domain.Project;
import com.pengwingscorp.halo.project.web.dto.ProjectResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ListProjects {
    private final ProjectRepository projectRepository;

    public ListProjects(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ArrayList<ProjectResponse> execute() {
        List<Project> projects = this.projectRepository.findAll();
        ArrayList<ProjectResponse> listProjectsResponse = new ArrayList<ProjectResponse>();
        projects.forEach((project) -> listProjectsResponse.add(new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreateAt()
        )));

        return listProjectsResponse;
    }
}
