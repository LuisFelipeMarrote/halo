package com.pengwingscorp.halo.task.infrastructure;

import com.pengwingscorp.halo.task.domain.EnumTaskStatus;
import com.pengwingscorp.halo.task.domain.Task;

import java.util.List;
import java.util.UUID;

public interface TaskRepository {
    UUID save(Task task);
    List<Task> findAll(UUID project_id, EnumTaskStatus status); // sem paginação mesmo, fodasse
    Task findById(UUID project_id, UUID task_id);
    Task update(Task task);
    boolean delete(UUID id);
}
