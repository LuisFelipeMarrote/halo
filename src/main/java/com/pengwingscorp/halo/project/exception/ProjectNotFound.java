package com.pengwingscorp.halo.project.exception;

public class ProjectNotFound extends RuntimeException{

    public ProjectNotFound(String id) {
        super("Project by id: '" + id + "' was not found");
    }
}
