package com.pengwingscorp.halo.task.application;

import com.pengwingscorp.halo.task.domain.EnumTaskStatus;
import com.pengwingscorp.halo.task.domain.Task;
import com.pengwingscorp.halo.task.exception.TaskNotFound;
import com.pengwingscorp.halo.task.infrastructure.TaskRepository;
import com.pengwingscorp.halo.task.web.dto.TaskRequest;
import com.pengwingscorp.halo.task.web.dto.TaskResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChangeTaskStatus {
    private final TaskRepository taskRepository;

    public ChangeTaskStatus(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void execute(UUID project_id, UUID task_id, @Nullable EnumTaskStatus status) {
        Task task = this.taskRepository.findById(project_id, task_id);
        if (task == null) {
            throw new TaskNotFound(task_id.toString());
        }
        task.changeStatus(status);
        this.taskRepository.update(task);
    }
}
