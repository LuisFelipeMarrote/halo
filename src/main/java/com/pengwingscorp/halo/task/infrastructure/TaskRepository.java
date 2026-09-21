package com.pengwingscorp.halo.task.infrastructure;

import com.pengwingscorp.halo.task.infrastructure.persistence.TaskJpaEntity;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskJpaEntity, UUID> {
    public Optional<TaskJpaEntity> findByIdAndProjectId(UUID id, UUID project_id);
    @Query("""
        SELECT tje FROM TaskJpaEntity tje
        WHERE tje.project.id = :projectId AND
            (:status IS NULL OR tje.status = :status)
    """)
    public List<TaskJpaEntity> findAllByProjectIdAndStatus(@Param("projectId") UUID project_id, @Param("status") @Nullable String status);
    public boolean existsByProjectId(UUID project_id);
}
