package com.pengwingscorp.halo.project.domain;

import com.pengwingscorp.halo.exception.InvalidStringContent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTest {
    private final String validName = "Projeto";
    private final String validDescription = "Projeto com descrição";

    @Test
    void validProject() {
        Project project = new Project.Builder()
                .setNome(validName)
                .setDescription(validDescription)
                .build();

        assertNotNull(project);
        assertEquals(Project.class, project.getClass());
        assertEquals(project.getName(), validName);
        assertEquals(project.getDescription(), validDescription);
    }

    @Test
    void invalidProjectName() {
        String name = "";
        assertThrowsExactly(InvalidStringContent.class, () -> {
            new Project.Builder()
                    .setNome(name)
                    .setDescription(validDescription)
                    .build();
        });
    }

    @Test
    void invalidProjectDescription() {
        String description = "";
        assertThrowsExactly(InvalidStringContent.class, () -> {
            new Project.Builder()
                    .setNome(validName)
                    .setDescription(description)
                    .build();
        });
    }
}