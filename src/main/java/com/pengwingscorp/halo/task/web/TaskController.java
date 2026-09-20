package com.pengwingscorp.halo.task.web;

import com.pengwingscorp.halo.task.application.*;
import com.pengwingscorp.halo.task.domain.EnumTaskStatus;
import com.pengwingscorp.halo.task.exception.InvalidTaskStatusArg;
import com.pengwingscorp.halo.task.infrastructure.TaskRepository;
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

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse create(@PathVariable("projectId") String projectId, @RequestBody TaskRequest taskRequest) {
        CreateTask createTask = new CreateTask(this.taskRepository);
        return createTask.execute(UUID.fromString(projectId), taskRequest);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<TaskResponse> list(@PathVariable("projectId") String projectId, @RequestParam(value = "status", required = false) String status) {
        if(!EnumTaskStatus.isValidType(status)) throw new InvalidTaskStatusArg(status);
        ListTasks listTask = new ListTasks(this.taskRepository);
        return listTask.execute(UUID.fromString(projectId), EnumTaskStatus.valueOf(status));
    }

    @GetMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponse getById(@PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId) {
        GetTaskById getTaskById = new GetTaskById(this.taskRepository);
        return getTaskById.execute(UUID.fromString(projectId), UUID.fromString(taskId));
    }

    @PutMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponse update(@PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId, @RequestBody TaskRequest taskRequest) {
        UpdateTask updateTask = new UpdateTask(this.taskRepository);
        return updateTask.execute(UUID.fromString(projectId), UUID.fromString(taskId), taskRequest);
    }

    @PatchMapping("/{taskId}/status")
    @ResponseStatus(HttpStatus.OK)
    public void changeStatus(@PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId, @RequestBody ChangeTaskStatusRequest dto) {
        if(!EnumTaskStatus.isValidType(dto.status())) throw new InvalidTaskStatusArg(dto.status());
        ChangeTaskStatus changeTaskStatus = new ChangeTaskStatus(this.taskRepository);
        changeTaskStatus.execute(UUID.fromString(projectId), UUID.fromString(taskId), EnumTaskStatus.valueOf(dto.status()));
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId) {
        DeleteTask deleteTask = new DeleteTask(this.taskRepository);
        deleteTask.execute(UUID.fromString(projectId), UUID.fromString(taskId));
    }
}
