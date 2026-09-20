package com.pengwingscorp.halo.project.infrastructure.persistence;

import com.pengwingscorp.halo.project.domain.Project;

public class ProjectJpaMapper {

    public static Project toDomain(ProjectJpaEntity projectJpaEntity) {
        return new Project(
                projectJpaEntity.getId(),
                projectJpaEntity.getName(),
                projectJpaEntity.getDescription(),
                projectJpaEntity.getCreateAt()
        );
    }

    public static ProjectJpaEntity toDatabase(Project project) {
        return new ProjectJpaEntity(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreateAt()
        );
    }
}



