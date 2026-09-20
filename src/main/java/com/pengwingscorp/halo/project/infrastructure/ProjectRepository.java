package com.pengwingscorp.halo.project.infrastructure;

import com.pengwingscorp.halo.project.domain.Project;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository {
    UUID save(Project project);
    List<Project> findAll(); // sem paginação mesmo, fodasse
    Project findById(UUID id);
    Project update(Project project);
    boolean delete(UUID id);
}
