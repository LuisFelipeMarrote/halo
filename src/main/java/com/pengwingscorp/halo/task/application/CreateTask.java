package com.pengwingscorp.halo.task.application;

import com.pengwingscorp.halo.task.domain.EnumTaskStatus;
import com.pengwingscorp.halo.task.domain.Task;
import com.pengwingscorp.halo.task.infrastructure.TaskRepository;
import com.pengwingscorp.halo.task.web.dto.TaskRequest;
import com.pengwingscorp.halo.task.web.dto.TaskResponse;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class CreateTask {
    private final TaskRepository taskRepository;

    public CreateTask(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponse execute(UUID projectId, TaskRequest taskRequest) {
        Task newTask = new Task.Builder()
                .setCreateAt(new Date())
                .setProject_id(projectId)
                .setDescription(taskRequest.description())
                .setStatus(EnumTaskStatus.PENDING)
                .setTitle(taskRequest.title())
                .build();
        UUID taskId = this.taskRepository.save(newTask);
        return new TaskResponse(
                taskId,
                newTask.getTitle(),
                newTask.getDescription(),
                newTask.getStatus(),
                newTask.getProject_id(),
                newTask.getCreateAt()
        );
    }
}
