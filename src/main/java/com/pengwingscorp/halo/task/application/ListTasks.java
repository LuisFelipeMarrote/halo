package com.pengwingscorp.halo.task.application;

import com.pengwingscorp.halo.task.domain.EnumTaskStatus;
import com.pengwingscorp.halo.task.infrastructure.TaskRepository;
import com.pengwingscorp.halo.task.infrastructure.persistence.TaskJpaEntity;
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
        String filter = (statusFilter == null) ? null : statusFilter.getDescription();
        List<TaskJpaEntity> taskJpaEntityList = this.taskRepository.findAllByProjectIdAndStatus(project_id, filter);
        ArrayList<TaskResponse> listTasksResponse = new ArrayList<TaskResponse>();
        taskJpaEntityList.forEach((taskJpaEntity) -> listTasksResponse.add(new TaskResponse(
                taskJpaEntity.getId(),
                taskJpaEntity.getTitle(),
                taskJpaEntity.getDescription(),
                taskJpaEntity.getStatus(),
                taskJpaEntity.getProject().getId(),
                taskJpaEntity.getCreateAt()
        )));
        return listTasksResponse;
    }
}
