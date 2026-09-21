package com.pengwingscorp.halo.project.infrastructure;

import com.pengwingscorp.halo.project.infrastructure.persistence.ProjectJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectJpaEntity, UUID> {}
