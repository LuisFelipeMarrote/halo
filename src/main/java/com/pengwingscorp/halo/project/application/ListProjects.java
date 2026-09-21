package com.pengwingscorp.halo.project.application;

import com.pengwingscorp.halo.project.infrastructure.ProjectRepository;
import com.pengwingscorp.halo.project.infrastructure.persistence.ProjectJpaEntity;
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
        List<ProjectJpaEntity> projectJpaEntityList = this.projectRepository.findAll();
        ArrayList<ProjectResponse> listProjectsResponse = new ArrayList<ProjectResponse>();
        projectJpaEntityList.forEach((projectJpaEntity) -> listProjectsResponse.add(new ProjectResponse(
                projectJpaEntity.getId(),
                projectJpaEntity.getName(),
                projectJpaEntity.getDescription(),
                projectJpaEntity.getCreateAt()
        )));

        return listProjectsResponse;
    }
}
