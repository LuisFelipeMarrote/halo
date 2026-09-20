package com.pengwingscorp.halo.task.application;

import com.pengwingscorp.halo.task.domain.EnumTaskStatus;
import com.pengwingscorp.halo.task.domain.Task;
import com.pengwingscorp.halo.task.exception.TaskNotFound;
import com.pengwingscorp.halo.task.infrastructure.TaskRepository;
import com.pengwingscorp.halo.task.web.dto.CreateTaskResponse;
import com.pengwingscorp.halo.task.web.dto.TaskRequest;
import com.pengwingscorp.halo.task.web.dto.TaskResponse;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class UpdateTask {
    private final TaskRepository taskRepository;

    public UpdateTask(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponse execute(UUID project_id, UUID task_id, TaskRequest taskRequest) {
        Task task = this.taskRepository.findById(project_id, task_id);
        if (task == null ) {
            throw new TaskNotFound(task_id.toString());
        }
        task.update(taskRequest.title(), taskRequest.description());
        Task updatedTask = this.taskRepository.update(task);
        return new TaskResponse(
                updatedTask.getId(),
                updatedTask.getTitle(),
                updatedTask.getDescription(),
                updatedTask.getStatus(),
                updatedTask.getProject_id(),
                updatedTask.getCreateAt()

        );
    }
}
