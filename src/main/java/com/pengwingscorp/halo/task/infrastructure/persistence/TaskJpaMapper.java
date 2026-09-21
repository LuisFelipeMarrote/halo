package com.pengwingscorp.halo.task.infrastructure.persistence;

import com.pengwingscorp.halo.project.infrastructure.persistence.ProjectJpaEntity;
import com.pengwingscorp.halo.task.domain.EnumTaskStatus;
import com.pengwingscorp.halo.task.domain.Task;

public class TaskJpaMapper {
    public static Task toDomain(TaskJpaEntity taskJpaEntity) {
        return new Task(
                taskJpaEntity.getTitle(),
                taskJpaEntity.getDescription(),
                EnumTaskStatus.valueOf(taskJpaEntity.getStatus()),
                taskJpaEntity.getProject().getId(),
                taskJpaEntity.getCreateAt(),
                taskJpaEntity.getId()
        );
    }

    public static TaskJpaEntity toDatabase(Task task, ProjectJpaEntity projectJpaEntity) {
        return new TaskJpaEntity(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                projectJpaEntity,
                task.getCreateAt()
        );
    }
}
