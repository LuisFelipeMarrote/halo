package com.pengwingscorp.halo.project.web;

import com.pengwingscorp.halo.project.infrastructure.ProjectRepository;
import com.pengwingscorp.halo.project.infrastructure.persistence.ProjectJpaEntity;
import com.pengwingscorp.halo.project.web.dto.ProjectRequest;
import com.pengwingscorp.halo.task.infrastructure.TaskRepository;
import com.pengwingscorp.halo.task.infrastructure.persistence.TaskJpaEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ApiProjectTest {

    private final MockMvc mockMvc;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final ObjectMapper objectMapper;

    public ApiProjectTest(MockMvc mockMvc, ProjectRepository projectRepository, TaskRepository taskRepository, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.objectMapper = objectMapper;
    }

    @Test
    void shouldReturn404NotFoundProject() throws Exception {
        // Arrange
        UUID project_id = UUID.fromString("c4f82637-25e1-45ea-9140-5a3d4f19b260");
        boolean exists = projectRepository.existsById(project_id);
        assertFalse(exists);
        // Act & Assert
        mockMvc.perform(get(
                "/v1/projects/{projectId}",
                        project_id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("PROJECT_NOT_FOUND"));
    }

    @Test
    void shouldReturn201CreateProject() throws Exception {
        // Arrange
        ProjectRequest projectRequest = new ProjectRequest("fast project", "new project");
        // Act & Assert
        mockMvc.perform(post("/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.createAt").exists())
                .andExpect(jsonPath("$.name").value("fast project"))
                .andExpect(jsonPath("$.description").value("new project"));
    }

    @Test
    void shouldReturn400CreateProject() throws Exception {
        // Arrange
        record MalformedProjectRequest(String description) {}
        MalformedProjectRequest malformedProjectRequest = new MalformedProjectRequest("fast project");
        // Act & Assert
        mockMvc.perform(post("/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(malformedProjectRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"));
    }

    @Test
    void shouldReturn200GetProjectById() throws Exception {
        // Arrange
        ProjectJpaEntity projectJpaEntity = createProject();

        // Act & Assert
        mockMvc.perform(get("/v1/projects/{id}", projectJpaEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectJpaEntity.getId().toString()))
                .andExpect(jsonPath("$.createAt").value(projectJpaEntity.getCreateAt().toInstant().toString()))
                .andExpect(jsonPath("$.name").value(projectJpaEntity.getName()))
                .andExpect(jsonPath("$.description").value(projectJpaEntity.getDescription()));
    }

    @Test
    void shouldReturn204DeleteProject() throws Exception {
        // Arrange
        ProjectJpaEntity projectJpaEntity = createProject();

        // Act & Assert
        mockMvc.perform(delete("/v1/projects/{id}", projectJpaEntity.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn409ProjectCannotBeDestroyed() throws Exception {
        // Arrange
        ProjectJpaEntity projectJpaEntity = createProject();
        TaskJpaEntity taskJpaEntity = new TaskJpaEntity(
                null,
                "título legal",
                "descrição legal",
                "PENDING",
                projectJpaEntity,
                new Date()
        );

        taskRepository.saveAndFlush(taskJpaEntity);

        // Act & Assert
        mockMvc.perform(delete("/v1/projects/{id}", projectJpaEntity.getId()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("PROJECT_CANNOT_BE_DESTROYED"));

    }


    ProjectJpaEntity createProject() {
        return projectRepository.saveAndFlush(new ProjectJpaEntity(
                null,
                "fast project",
                "new project",
                new Date()
        ));
    }
}