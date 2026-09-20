package com.pengwingscorp.halo.project.web;

import com.pengwingscorp.halo.project.application.*;
import com.pengwingscorp.halo.project.infrastructure.ProjectRepository;
import com.pengwingscorp.halo.project.web.dto.*;
import com.pengwingscorp.halo.task.infrastructure.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/v1/projects")
public class ProjectController {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public ProjectController(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse createProject(@RequestBody ProjectRequest projectRequest) {
        CreateProject createProject = new CreateProject(projectRepository);
        return createProject.execute(projectRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ArrayList<ProjectResponse> listProjects() {
        ListProjects listProjects = new ListProjects(projectRepository);
        return listProjects.execute();
    }

    @GetMapping(value ="/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectResponse getProjectById(@PathVariable("id") String id) {
        GetProjectById getProjectById = new GetProjectById(projectRepository);
        // tenho que colocar uma validação se id não conseguir transformar no tipo UUID
        // e tratamento de erro
        return getProjectById.execute(new GetProjectRequest(UUID.fromString(id)));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectResponse updateProject(@PathVariable("id") String id, @RequestBody ProjectRequest projectRequest) {
        UpdateProject updateProject = new UpdateProject(projectRepository);
        return updateProject.execute(UUID.fromString(id), projectRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable("id") String id) {
        DeleteProject deleteProject = new DeleteProject(projectRepository, taskRepository);
        deleteProject.execute(UUID.fromString(id));
    }
}
