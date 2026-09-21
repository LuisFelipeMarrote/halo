package com.pengwingscorp.halo.task.web;

import com.pengwingscorp.halo.task.application.*;
import com.pengwingscorp.halo.task.domain.EnumTaskStatus;
import com.pengwingscorp.halo.task.exception.InvalidTaskStatusArg;
import com.pengwingscorp.halo.task.web.dto.ChangeTaskStatusRequest;
import com.pengwingscorp.halo.task.web.dto.TaskRequest;
import com.pengwingscorp.halo.task.web.dto.TaskResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/projects/{projectId}/tasks")
public class TaskController {
    private final ChangeTaskStatus changeTaskStatus;
    private final CreateTask createTask;
    private final DeleteTask deleteTask;
    private final GetTaskById getTaskById;
    private final ListTasks listTasks;
    private final UpdateTask updateTask;

    public TaskController(
            ChangeTaskStatus changeTaskStatus,
            CreateTask createTask,
            DeleteTask deleteTask,
            GetTaskById getTaskById,
            ListTasks listTasks,
            UpdateTask updateTask
    ) {
        this.changeTaskStatus = changeTaskStatus;
        this.createTask = createTask;
        this.deleteTask = deleteTask;
        this.getTaskById = getTaskById;
        this.listTasks = listTasks;
        this.updateTask = updateTask;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse create(@PathVariable("projectId") String projectId, @RequestBody TaskRequest taskRequest) {
        return createTask.execute(UUID.fromString(projectId), taskRequest);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<TaskResponse> list(@PathVariable("projectId") String projectId, @RequestParam(value = "status", required = false) String status) {
        EnumTaskStatus filter = (status == null)
                ? null
                : (EnumTaskStatus.isValidType(status)
                    ? EnumTaskStatus.valueOf(status)
                    : throwInvalidTaskStatusArg(status));
        return listTasks.execute(UUID.fromString(projectId), filter);
    }

    private EnumTaskStatus throwInvalidTaskStatusArg(String s) {
        throw new InvalidTaskStatusArg(s);
    }

    @GetMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponse getById(@PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId) {
        return getTaskById.execute(UUID.fromString(projectId), UUID.fromString(taskId));
    }

    @PutMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponse update(@PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId, @RequestBody TaskRequest taskRequest) {
        return updateTask.execute(UUID.fromString(projectId), UUID.fromString(taskId), taskRequest);
    }

    @PatchMapping("/{taskId}/status")
    @ResponseStatus(HttpStatus.OK)
    public void changeStatus(@PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId, @RequestBody ChangeTaskStatusRequest dto) {
        if(!EnumTaskStatus.isValidType(dto.status())) throw new InvalidTaskStatusArg(dto.status());
        changeTaskStatus.execute(UUID.fromString(projectId), UUID.fromString(taskId), EnumTaskStatus.valueOf(dto.status()));
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId) {
        deleteTask.execute(UUID.fromString(projectId), UUID.fromString(taskId));
    }
}
