package com.pengwingscorp.halo.task.application;

import com.pengwingscorp.halo.task.exception.TaskNotFound;
import com.pengwingscorp.halo.task.infrastructure.TaskRepository;
import com.pengwingscorp.halo.task.infrastructure.persistence.TaskJpaEntity;
import com.pengwingscorp.halo.task.web.dto.TaskResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetTaskById {
    private final TaskRepository taskRepository;

    public GetTaskById(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponse execute(UUID project_id, UUID task_id) {
        TaskJpaEntity taskJpaEntity = taskRepository
                .findByIdAndProjectId(task_id, project_id)
                .orElseThrow(() -> new TaskNotFound(task_id.toString()));

        return new TaskResponse(
                taskJpaEntity.getId(),
                taskJpaEntity.getTitle(),
                taskJpaEntity.getDescription(),
                taskJpaEntity.getStatus(),
                taskJpaEntity.getProject().getId(),
                taskJpaEntity.getCreateAt()
        );
    }
}
