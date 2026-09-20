package com.pengwingscorp.halo.task.infrastructure;

import com.pengwingscorp.halo.task.domain.EnumTaskStatus;
import com.pengwingscorp.halo.task.domain.Task;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class InMemoryTaskRepository implements TaskRepository {
    @Override
    public UUID save(Task task) {
        return UUID.fromString("33333333-3333-3333-3333-333333333333");
    }

    @Override
    public List<Task> findAll(UUID project_id, EnumTaskStatus status) {
        return List.of();
    }

    @Override
    public Task findById(UUID project_id, UUID task_id) {
        return null;
    }

    @Override
    public Task update(Task task) {
        return null;
    }

    @Override
    public boolean delete(UUID id) {
        return true;
    }
}
