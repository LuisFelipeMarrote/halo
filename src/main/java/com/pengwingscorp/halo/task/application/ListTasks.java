package com.pengwingscorp.halo.task.application;

import com.pengwingscorp.halo.task.domain.EnumTaskStatus;
import com.pengwingscorp.halo.task.domain.Task;
import com.pengwingscorp.halo.task.infrastructure.TaskRepository;
import com.pengwingscorp.halo.task.web.dto.TaskResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ListTasks {
    private final TaskRepository taskRepository;

    public ListTasks(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * @param statusFilter status to filter by, or {@code null} to include all statuses
     */
    public List<TaskResponse> execute(UUID project_id, @Nullable EnumTaskStatus statusFilter) {
        List<Task> tasks = this.taskRepository.findAll(project_id, statusFilter);
        ArrayList<TaskResponse> listTasksResponse = new ArrayList<TaskResponse>();
        tasks.forEach((task) -> listTasksResponse.add(new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getProject_id(),
                task.getCreateAt()
        )));
        return listTasksResponse;
    }
}
