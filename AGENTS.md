# Repository context

This file applies to the whole repository. Use it as a starting map for future chats; verify details in the current source before making changes.

## Purpose and sources of context
- Halo is a Java/Spring learning project for a REST API that manages projects and their tasks.
- Read `ESPECIFICACAO.md` (Portuguese, UTF-8) for intended business rules, API contracts, scope, and completion criteria. It describes a target, not completed functionality.
- Read `pom.xml` for dependencies and versions, and the relevant controllers, DTOs, use cases, and repositories for actual behavior.
- The specification and implementation differ: the specification uses Portuguese routes, Long IDs, and JPA/H2; current code uses English `/v1` routes, UUID IDs, and repository placeholders. Do not silently rename contracts or migrate architecture as an unrelated cleanup.
- The first-version specification excludes authentication, users, a web UI, pagination, notifications, deployment, and Docker. Keep work within the requested scope.

## Stack and navigation
- Java 21, Spring Boot 4.1.1, Maven wrapper. Dependencies currently include Spring MVC, Bean Validation, development tools, and test starters; JPA/H2 are not yet configured.
- Entry point: `src/main/java/com/pengwingscorp/halo/HaloApplication.java`.
- Under `src/main/java/com/pengwingscorp/halo/`, `project/` and `task/` each contain `domain/`, `application/`, `infrastructure/`, `web/`, `web/dto/`, and `exception/`.
- `application/` contains individual use-case classes such as `CreateProject` and `ListTasks`, usually exposing `execute(...)`.
- `infrastructure/` contains repository interfaces and their `InMemory...` implementations. Inspect these before assuming persistence works.
- Shared identity lives in `baseEntities/Entity.java`; centralized HTTP error handling lives in `exception/GlobalExceptionHandler.java` and `ErrorResponse.java`.
- Configuration: `src/main/resources/application.yaml`. Tests: `src/test/java/com/pengwingscorp/halo/`.

## Current implementation caveats
- Project routes start at `/v1/projects`; task routes start at `/v1/projects/{projectId}/tasks`.
- Project update and delete currently use bodies at the collection route. TaskController currently exposes create and list only; a use-case class alone does not mean an endpoint exists.
- Controllers inject repositories and manually construct use cases. Follow the surrounding structure unless the task calls for a dependency-injection refactor.
- Domain objects currently use builders, UUID identity, and `java.util.Date`; they are not JPA entities.
- The project repository is a stub: save returns a fixed UUID, list returns an empty collection, and other operations return null or do nothing. Do not describe the API as fully implemented.
- Validation, error handling, task filtering, and project/task ownership rules need inspection before relying on them. An optional query parameter annotation does not guarantee the absent-value path works.
- The existing test is a Spring context-loading smoke test; it does not establish API or business-rule correctness.

## Working and verification
- Preserve the feature-based package layout, use DTOs at HTTP boundaries, and keep business logic in application/domain code and data access behind repositories.
- Check the specification's relevant business rules when changing behavior, including normalization, ownership, initial status, and blocking deletion of projects with tasks.
- From the repository root on Windows: `.\mvnw.cmd test` runs tests, `.\mvnw.cmd verify` runs lifecycle checks, and `.\mvnw.cmd spring-boot:run` starts the application.
- On Unix-like systems, use `./mvnw` in place of `.\mvnw.cmd`. A compatible JDK is required; the wrapper may need network access to download Maven/dependencies.
- Run checks appropriate to the change. For behavior changes, add focused tests for the affected rules; documentation-only changes do not require an application build. Report checks actually run and any blockers.
- Inspect local changes before editing and preserve unrelated work. If Git metadata is unavailable, do not initialize a repository merely to inspect status.
- Keep this file concise and update it when architecture, commands, or major implementation gaps change. Record durable facts, not speculative plans, secrets, or transcripts of chats.
