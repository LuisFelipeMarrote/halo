package com.pengwingscorp.halo.task.exception;

public class TaskNotFound extends RuntimeException{

    public TaskNotFound(String task_id) {
        super("Task by id: '" + task_id + "' was not found");
    }
}
