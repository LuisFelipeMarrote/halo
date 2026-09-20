package com.pengwingscorp.halo.project.exception;

public class ProjectCannotBeDestroyed extends RuntimeException{

    public ProjectCannotBeDestroyed(String id) {
        super("Project by id: '" + id + "' still has task(s) inside");
    }
}
