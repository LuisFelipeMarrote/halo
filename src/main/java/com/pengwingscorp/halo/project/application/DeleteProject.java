package com.pengwingscorp.halo.project.application;

import com.pengwingscorp.halo.project.exception.ProjectCannotBeDestroyed;
import com.pengwingscorp.halo.project.exception.ProjectNotFound;
import com.pengwingscorp.halo.project.infrastructure.ProjectRepository;
import com.pengwingscorp.halo.project.infrastructure.persistence.ProjectJpaEntity;
import com.pengwingscorp.halo.task.infrastructure.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteProject {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public DeleteProject(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional
    public void execute(UUID id) {
        ProjectJpaEntity projectJpaEntity = projectRepository
                .findById(id)
                .orElseThrow(() -> new ProjectNotFound(id.toString()));
        if(taskRepository.existsByProjectId(id)) throw new ProjectCannotBeDestroyed(id.toString());
        projectRepository.delete(projectJpaEntity);
    }
}
