package com.pengwingscorp.halo.task.application;

import com.pengwingscorp.halo.task.domain.Task;
import com.pengwingscorp.halo.task.exception.TaskNotFound;
import com.pengwingscorp.halo.task.infrastructure.TaskRepository;
import com.pengwingscorp.halo.task.infrastructure.persistence.TaskJpaEntity;
import com.pengwingscorp.halo.task.infrastructure.persistence.TaskJpaMapper;
import com.pengwingscorp.halo.task.web.dto.TaskRequest;
import com.pengwingscorp.halo.task.web.dto.TaskResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateTask {
    private final TaskRepository taskRepository;

    public UpdateTask(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponse execute(UUID project_id, UUID task_id, TaskRequest taskRequest) {
        TaskJpaEntity taskJpaEntity = this.taskRepository
                .findByIdAndProjectId(task_id, project_id)
                .orElseThrow(() -> new TaskNotFound(task_id.toString()));
        Task task = TaskJpaMapper.toDomain(taskJpaEntity);
        task.update(taskRequest.title(), taskRequest.description());
        TaskJpaEntity updatedTaskJpaEntity = this.taskRepository.save(TaskJpaMapper.toDatabase(task, taskJpaEntity.getProject()));
        return new TaskResponse(
                updatedTaskJpaEntity.getId(),
                updatedTaskJpaEntity.getTitle(),
                updatedTaskJpaEntity.getDescription(),
                updatedTaskJpaEntity.getStatus(),
                updatedTaskJpaEntity.getProject().getId(),
                updatedTaskJpaEntity.getCreateAt()

        );
    }
}
