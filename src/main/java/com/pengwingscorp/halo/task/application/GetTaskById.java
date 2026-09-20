package com.pengwingscorp.halo.task.application;

import com.pengwingscorp.halo.task.domain.Task;
import com.pengwingscorp.halo.task.exception.TaskNotFound;
import com.pengwingscorp.halo.task.infrastructure.TaskRepository;
import com.pengwingscorp.halo.task.web.dto.TaskResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GetTaskById {
    private final TaskRepository taskRepository;

    public GetTaskById(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponse execute(UUID project_id, UUID task_id) {
        Task task = this.taskRepository.findById(project_id, task_id);
        if (task == null) {
            throw new TaskNotFound(task_id.toString());
        }
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getProject_id(),
                task.getCreateAt()
        );
    }
}
