package com.pengwingscorp.halo.task.application;

import com.pengwingscorp.halo.project.infrastructure.ProjectRepository;
import com.pengwingscorp.halo.task.domain.EnumTaskStatus;
import com.pengwingscorp.halo.task.domain.Task;
import com.pengwingscorp.halo.task.exception.TaskNotFound;
import com.pengwingscorp.halo.task.infrastructure.TaskRepository;
import com.pengwingscorp.halo.task.infrastructure.persistence.TaskJpaEntity;
import com.pengwingscorp.halo.task.infrastructure.persistence.TaskJpaMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChangeTaskStatus {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public ChangeTaskStatus(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    public void execute(UUID project_id, UUID task_id,@NotNull EnumTaskStatus status) {
        TaskJpaEntity taskJpaEntity = taskRepository
                .findByIdAndProjectId(task_id, project_id)
                .orElseThrow(() -> new TaskNotFound(task_id.toString()));
        Task task = TaskJpaMapper.toDomain(taskJpaEntity);
        task.changeStatus(status);
        TaskJpaEntity changedTaskJpaEntity = TaskJpaMapper.toDatabase(task, projectRepository.getReferenceById(project_id));
        taskRepository.save(changedTaskJpaEntity);
    }
}
