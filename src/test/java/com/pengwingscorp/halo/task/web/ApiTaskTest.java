package com.pengwingscorp.halo.task.web;

import com.pengwingscorp.halo.project.infrastructure.ProjectRepository;
import com.pengwingscorp.halo.project.infrastructure.persistence.ProjectJpaEntity;
import com.pengwingscorp.halo.task.infrastructure.TaskRepository;
import com.pengwingscorp.halo.task.infrastructure.persistence.TaskJpaEntity;
import com.pengwingscorp.halo.task.web.dto.ChangeTaskStatusRequest;
import com.pengwingscorp.halo.task.web.dto.TaskRequest;
import com.pengwingscorp.halo.user.application.LoginUser;
import com.pengwingscorp.halo.user.application.RegisterUser;
import com.pengwingscorp.halo.user.domain.UserRole;
import com.pengwingscorp.halo.user.web.dto.AuthenticationDTO;
import com.pengwingscorp.halo.user.web.dto.TokenDTO;
import com.pengwingscorp.halo.user.web.dto.UserRequest;
import com.pengwingscorp.halo.user.web.dto.UserResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ApiTaskTest {
    private final MockMvc mockMvc;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final ObjectMapper objectMapper;
    private final RegisterUser registerUser;
    private final LoginUser loginUser;
    private String token;

    public ApiTaskTest(
            MockMvc mockMvc,
            ProjectRepository projectRepository,
            TaskRepository taskRepository,
            ObjectMapper objectMapper, RegisterUser registerUser, LoginUser loginUser
    ) {
        this.mockMvc = mockMvc;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.objectMapper = objectMapper;
        this.registerUser = registerUser;
        this.loginUser = loginUser;
    }

    @BeforeAll
    void getToken() {
        String login = "luis";
        String password = "oi123";
        UserRequest userRequest = new UserRequest(
                login,
                password,
                UserRole.ADMIN
        );
        UserResponse userResponse = registerUser.execute(userRequest);
        TokenDTO tokenDto = loginUser.execute(new AuthenticationDTO(login, password));
        token = tokenDto.token();
    }

    @Test
    void shouldReturn404NotFoundTask() throws Exception {
        // Arrange
        ProjectJpaEntity projectJpaEntity = createProject();
        UUID task_id = UUID.fromString("c4f82637-25e1-45ea-9140-5a3d4f19b260");
        boolean exists = taskRepository.existsById(task_id);
        assertFalse(exists);
        // Act & Assert
        mockMvc.perform(get("/v1/projects/{projectId}/tasks/{taskId}", projectJpaEntity.getId(), task_id)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("TASK_NOT_FOUND"));
    }

    @Test
    void shouldReturn201CreateTask() throws Exception {
        // Arrange
        ProjectJpaEntity projectJpaEntity = createProject();
        TaskRequest taskRequest = new TaskRequest("fast task", "new task");
        // Act & Assert
        mockMvc.perform(post("/v1/projects/{projectId}/tasks", projectJpaEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest))
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.createAt").exists())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.project_id").value(projectJpaEntity.getId().toString()))
                .andExpect(jsonPath("$.title").value("fast task"))
                .andExpect(jsonPath("$.description").value("new task"));
    }

    @Test
    void shouldReturn400CreateTask() throws Exception {
        // Arrange
        ProjectJpaEntity projectJpaEntity = createProject();
        record MalformedTaskRequest(String description) {}
        MalformedTaskRequest malformedProjectRequest = new MalformedTaskRequest("fast task");
        // Act & Assert
        mockMvc.perform(post("/v1/projects/{projectId}/tasks", projectJpaEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(malformedProjectRequest))
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"));
    }

    @Test
    void shouldReturn200UpdateTask() throws Exception {
        // Arrange
        ProjectJpaEntity projectJpaEntity = createProject();
        TaskJpaEntity taskJpaEntity = createTask(projectJpaEntity);
        TaskRequest taskRequest = new TaskRequest("current title", "current description");
        // Act & Assert
        mockMvc.perform(put("/v1/projects/{projectId}/tasks/{taskId}", projectJpaEntity.getId(),
                            taskJpaEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest))
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskJpaEntity.getId().toString()))
                .andExpect(jsonPath("$.createAt").value(taskJpaEntity.getCreateAt().toInstant().toString()))
                .andExpect(jsonPath("$.status").value(taskJpaEntity.getStatus()))
                .andExpect(jsonPath("$.project_id").value(taskJpaEntity.getProject().getId().toString()))
                .andExpect(jsonPath("$.title").value(taskRequest.title()))
                .andExpect(jsonPath("$.description").value(taskRequest.description()));
    }

    @Test
    void shouldReturn200GetTaskById() throws Exception {
        // Arrange
        ProjectJpaEntity projectJpaEntity = createProject();
        TaskJpaEntity taskJpaEntity = createTask(projectJpaEntity);
        // Act & Assert
        mockMvc.perform(get("/v1/projects/{id}/tasks/{taskId}",
                            projectJpaEntity.getId(),
                            taskJpaEntity.getId())
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskJpaEntity.getId().toString()))
                .andExpect(jsonPath("$.createAt").value(taskJpaEntity.getCreateAt().toInstant().toString()))
                .andExpect(jsonPath("$.status").value(taskJpaEntity.getStatus()))
                .andExpect(jsonPath("$.project_id").value(taskJpaEntity.getProject().getId().toString()))
                .andExpect(jsonPath("$.title").value(taskJpaEntity.getTitle()))
                .andExpect(jsonPath("$.description").value(taskJpaEntity.getDescription()));
    }

    @Test
    void shouldReturn204DeleteTask() throws Exception {
        // Arrange
        ProjectJpaEntity projectJpaEntity = createProject();
        TaskJpaEntity taskJpaEntity = createTask(projectJpaEntity);
        // Act & Assert
        mockMvc.perform(delete("/v1/projects/{projectId}/tasks/{taskId}",
                            projectJpaEntity.getId(),
                            taskJpaEntity.getId())
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn200ChangeTaskStatus() throws Exception {
        // Arrange
        ProjectJpaEntity projectJpaEntity = createProject();
        TaskJpaEntity taskJpaEntity = createTask(projectJpaEntity);
        ChangeTaskStatusRequest changeTaskStatusRequest = new ChangeTaskStatusRequest("IN_PROGRESS");
        // Act
        mockMvc.perform(patch("/v1/projects/{projectId}/tasks/{taskId}/status",
                            projectJpaEntity.getId(),
                            taskJpaEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeTaskStatusRequest))
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk());
        // Assert
        TaskJpaEntity checkTaskJpaEntity = taskRepository.findByIdAndProjectId(
                    taskJpaEntity.getId(),
                    projectJpaEntity.getId())
                .orElseThrow(Exception::new);
        assertEquals("IN_PROGRESS", checkTaskJpaEntity.getStatus());
    }

    ProjectJpaEntity createProject() {
        return projectRepository.saveAndFlush(new ProjectJpaEntity(
                null,
                "fast project",
                "new project",
                new Date()
        ));
    }

    TaskJpaEntity createTask(ProjectJpaEntity projectJpaEntity) {
        return taskRepository.saveAndFlush(new TaskJpaEntity(
                null,
                "fast task",
                "new task",
                "PENDING",
                projectJpaEntity,
                new Date()
        ));
    }
}