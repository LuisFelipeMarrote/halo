package com.pengwingscorp.halo.task.exception;

public class InvalidTaskStatusArg extends RuntimeException {

    public InvalidTaskStatusArg(String message) {
        super("Value: "+ message + ", is an invalid argument for a task status");
    }
}
