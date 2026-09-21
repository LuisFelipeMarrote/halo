package com.pengwingscorp.halo.task.application;

import com.pengwingscorp.halo.project.exception.ProjectNotFound;
import com.pengwingscorp.halo.project.infrastructure.ProjectRepository;
import com.pengwingscorp.halo.task.domain.EnumTaskStatus;
import com.pengwingscorp.halo.task.domain.Task;
import com.pengwingscorp.halo.task.infrastructure.TaskRepository;
import com.pengwingscorp.halo.task.infrastructure.persistence.TaskJpaEntity;
import com.pengwingscorp.halo.task.infrastructure.persistence.TaskJpaMapper;
import com.pengwingscorp.halo.task.web.dto.TaskRequest;
import com.pengwingscorp.halo.task.web.dto.TaskResponse;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class CreateTask {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public CreateTask(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    public TaskResponse execute(UUID projectId, TaskRequest taskRequest) {
        boolean projectExists = projectRepository.existsById(projectId);
        if(!projectExists) throw new ProjectNotFound(projectId.toString());

        Task newTask = new Task.Builder()
                .setCreateAt(new Date())
                .setProject_id(projectId)
                .setDescription(taskRequest.description())
                .setStatus(EnumTaskStatus.PENDING)
                .setTitle(taskRequest.title())
                .build();
        TaskJpaEntity taskJpaEntity = taskRepository.save(TaskJpaMapper.toDatabase(newTask, projectRepository.getReferenceById(projectId)));
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
