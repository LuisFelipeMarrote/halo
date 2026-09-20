package com.pengwingscorp.halo.project.infrastructure;

import com.pengwingscorp.halo.project.domain.Project;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
public class InMemoryProjectRepository implements ProjectRepository{
    @Override
    public UUID save(Project project) {
        return UUID.fromString("11111111-1111-1111-1111-111111111111");
    }

    @Override
    public ArrayList<Project> findAll() {
        return new ArrayList<Project>();
    }

    @Override
    public Project findById(UUID id) {
        return null;
    }

    @Override
    public Project update(Project project) { return null; }

    @Override
    public boolean delete(UUID id) {
        return true;
    }
}