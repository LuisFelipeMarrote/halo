# Repository context

This file applies to the whole repository. Use it as a starting map for future chats; verify details in the current source before making changes.

## Purpose and sources of context
- Halo is a Java/Spring learning project for a REST API that manages projects and their tasks.
- Read `ESPECIFICACAO.md` (Portuguese, UTF-8) for intended business rules, API contracts, scope, and completion criteria. It describes a target, not completed functionality.
- Read `pom.xml` for dependencies and versions, and the relevant controllers, DTOs, use cases, and repositories for actual behavior.
- The specification and implementation differ: the specification uses Portuguese routes, Long IDs, and JPA/H2; current code uses English `/v1` routes, UUID IDs, and JPA/PostgreSQL. Do not silently rename contracts or migrate architecture as an unrelated cleanup.
- The first-version specification excludes authentication, users, a web UI, pagination, notifications, deployment, and Docker, but the implementation now includes Docker Compose for PostgreSQL. Preserve this existing setup and keep work within the requested scope.

## Stack and navigation
- Java 21, Spring Boot 4.1.1, Maven wrapper. Dependencies include Spring MVC, Bean Validation, Spring Data JPA, the PostgreSQL driver, Docker Compose integration, development tools, and test starters.
- Entry point: `src/main/java/com/pengwingscorp/halo/HaloApplication.java`.
- Under `src/main/java/com/pengwingscorp/halo/`, `project/` and `task/` each contain `domain/`, `application/`, `infrastructure/`, `web/`, `web/dto/`, and `exception/`.
- `application/` contains individual use-case classes such as `CreateProject` and `ListTasks`, usually exposing `execute(...)`.
- `infrastructure/` contains Spring Data repository interfaces; `infrastructure/persistence/` contains JPA entities and domain/persistence mappers. There are no current `InMemory...` repository implementations.
- Shared domain identity lives in `base_entities/Entity.java`; centralized HTTP error handling lives in `exception/GlobalExceptionHandler.java` and `ErrorResponse.java`.
- Configuration: `src/main/resources/application.yaml`. Tests: `src/test/java/com/pengwingscorp/halo/`.

## Persistence layer
- `ProjectRepository` extends `JpaRepository<ProjectJpaEntity, UUID>`; `TaskRepository` extends `JpaRepository<TaskJpaEntity, UUID>`. Spring Data supplies the implementations; repository methods operate on persistence entities, not domain objects.
- `ProjectJpaEntity` maps to `projects`; `TaskJpaEntity` maps to `tasks`. Both use `@GeneratedValue(strategy = GenerationType.UUID)` and `java.util.Date` creation timestamps. Domain builders leave IDs unset until persistence assigns them.
- Tasks have a mandatory lazy `@ManyToOne` project association through `project_id`. Projects have no active task collection, and no cascade operations are configured. Task status is stored as a String (`PENDING`, `IN_PROGRESS`, `DONE`) and converted to/from `EnumTaskStatus` by the domain/mapping code.
- `ProjectJpaMapper` and `TaskJpaMapper` convert between plain domain objects and JPA entities. Use cases perform these conversions and return DTOs; preserve this separation when changing persistence.
- Task queries include `findByIdAndProjectId` for ownership, `existsByProjectId`, and a JPQL `findAllByProjectIdAndStatus` query where null status means all statuses. Current list queries do not specify ordering.
- Main configuration uses `DATABASE_URL`, `DATABASE_USER`, and `DATABASE_PASSWORD`, the PostgreSQL dialect, and Hibernate `ddl-auto: update`. `docker-compose.yaml` defines PostgreSQL 15 Alpine with a persistent volume and also uses `DATABASE_DB` and `DATABASE_PORT`. Do not copy `.env` values into documentation.
- `UpdateProject` and `DeleteProject` explicitly use `@Transactional`; other use cases currently rely on repository transaction boundaries. Inspect lazy association access and multi-step operations before assuming an entire use case is transactional.

## Current implementation caveats
- Project routes start at `/v1/projects`; task routes start at `/v1/projects/{projectId}/tasks`.
- Project update and delete use `/{id}`. TaskController exposes create, list, get, update, status change, and delete; status change currently returns HTTP 200 with no body.
- Controllers constructor-inject use cases, which are `@Service` beans and constructor-inject repositories. Follow the surrounding structure unless the task calls for a dependency-injection refactor.
- Domain objects currently use builders, UUID identity, and `java.util.Date`; they are not JPA entities.
- `DeleteProject` currently has an inverted guard: it throws when `!taskRepository.existsByProjectId(id)`. This contradicts the specification's rule to block projects that have tasks.
- An absent task status filter is handled, but `ListTasks` does not verify that the project exists. Descriptions are non-null in both JPA mappings and required by domain builder validation, unlike the specification's optional descriptions. Validation and error handling still need inspection before claiming API completeness.
- Tests include a Spring context smoke test, basic project domain tests, and unfinished test methods. `ApiProjectTest` has constructor parameters but no Spring test setup or parameter resolver; there are no current repository integration tests or `@DataJpaTest` tests.
- Test database setup is inconsistent: `application-test.yaml` selects the H2 driver/dialect, but `pom.xml` has no H2 dependency, while `docker-compose-test.yaml` provisions PostgreSQL using `DATABASE_DB_TEST`. Existing tests do not activate the `test` profile. Both Compose files use the same container name and host-port variable, so do not assume they can run simultaneously.

## Working and verification
- Preserve the feature-based package layout, use DTOs at HTTP boundaries, and keep business logic in application/domain code and data access behind repositories.
- Check the specification's relevant business rules when changing behavior, including normalization, ownership, initial status, and blocking deletion of projects with tasks.
- From the repository root on Windows: `.\mvnw.cmd test` runs tests, `.\mvnw.cmd verify` runs lifecycle checks, and `.\mvnw.cmd spring-boot:run` starts the application.
- On Unix-like systems, use `./mvnw` in place of `.\mvnw.cmd`. A compatible JDK is required; the wrapper may need network access to download Maven/dependencies.
- Run checks appropriate to the change. For behavior changes, add focused tests for the affected rules; documentation-only changes do not require an application build. Report checks actually run and any blockers.
- Inspect local changes before editing and preserve unrelated work. If Git metadata is unavailable, do not initialize a repository merely to inspect status.
- Keep this file concise and update it when architecture, commands, or major implementation gaps change. Record durable facts, not speculative plans, secrets, or transcripts of chats.
