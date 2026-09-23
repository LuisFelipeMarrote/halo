package com.pengwingscorp.halo.project.web;

import com.pengwingscorp.halo.project.application.*;
import com.pengwingscorp.halo.project.web.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/v1/projects")
public class ProjectController {
    private final CreateProject createProject;
    private final DeleteProject deleteProject;
    private final GetProjectById getProjectById;
    private final ListProjects listProjects;
    private final UpdateProject updateProject;

    public ProjectController(CreateProject createProject, DeleteProject deleteProject, GetProjectById getProjectById, ListProjects listProjects, UpdateProject updateProject) {
        this.createProject = createProject;
        this.deleteProject = deleteProject;
        this.getProjectById = getProjectById;
        this.listProjects = listProjects;
        this.updateProject = updateProject;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse createProject(@Valid @RequestBody ProjectRequest projectRequest) {
        return createProject.execute(projectRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ArrayList<ProjectResponse> listProjects() {
        return listProjects.execute();
    }

    @GetMapping(value ="/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectResponse getProjectById(@PathVariable("id") String id) {
        // tenho que colocar uma validação se id não conseguir transformar no tipo UUID
        // e tratamento de erro
        return getProjectById.execute(UUID.fromString(id));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectResponse updateProject(@PathVariable("id") String id, @Valid @RequestBody ProjectRequest projectRequest) {
        return updateProject.execute(UUID.fromString(id), projectRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable("id") String id) {
        deleteProject.execute(UUID.fromString(id));
    }
}
