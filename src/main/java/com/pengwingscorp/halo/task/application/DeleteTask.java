package com.pengwingscorp.halo.task.application;

import com.pengwingscorp.halo.task.domain.Task;
import com.pengwingscorp.halo.task.exception.TaskNotFound;
import com.pengwingscorp.halo.task.infrastructure.TaskRepository;
import com.pengwingscorp.halo.task.web.dto.TaskRequest;
import com.pengwingscorp.halo.task.web.dto.TaskResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteTask {
    private final TaskRepository taskRepository;

    public DeleteTask(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void execute(UUID project_id, UUID task_id) {
        boolean hasDeleted = this.taskRepository.delete(task_id);
        if (!hasDeleted) {
            throw new TaskNotFound(task_id.toString());
        }
    }
}
