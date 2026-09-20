package com.pengwingscorp.halo.project.application;

import com.pengwingscorp.halo.project.exception.ProjectCannotBeDestroyed;
import com.pengwingscorp.halo.project.exception.ProjectNotFound;
import com.pengwingscorp.halo.project.infrastructure.ProjectRepository;
import com.pengwingscorp.halo.task.domain.Task;
import com.pengwingscorp.halo.task.infrastructure.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeleteProject {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public DeleteProject(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    public void execute(UUID id) {
        List<Task> taskList = taskRepository.findAll(id, null);
        if(!taskList.isEmpty()) throw new ProjectCannotBeDestroyed(id.toString());
        boolean hasDeleted = projectRepository.delete(id);
        if(!hasDeleted) throw new ProjectNotFound(id.toString());
    }
}
