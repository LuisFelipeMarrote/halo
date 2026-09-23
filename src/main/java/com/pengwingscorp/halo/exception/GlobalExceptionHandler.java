package com.pengwingscorp.halo.exception;

import com.pengwingscorp.halo.project.exception.ProjectCannotBeDestroyed;
import com.pengwingscorp.halo.project.exception.ProjectNotFound;
import com.pengwingscorp.halo.task.exception.InvalidTaskStatusArg;
import com.pengwingscorp.halo.task.exception.TaskNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProjectNotFound.class)
    public ResponseEntity<ErrorResponse> handleProjectNotFound(
            ProjectNotFound exception) {
        ErrorResponse error = new ErrorResponse(
                "PROJECT_NOT_FOUND",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(ProjectCannotBeDestroyed.class)
    public ResponseEntity<ErrorResponse> handleProjectCannotBeDestroyed(
            ProjectCannotBeDestroyed exception) {
        ErrorResponse error = new ErrorResponse(
                "PROJECT_CANNOT_BE_DESTROYED",
                exception.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(InvalidStringContent.class)
    public ResponseEntity<ErrorResponse> handleInvalidStringContent(
            InvalidStringContent exception) {
        ErrorResponse error = new ErrorResponse(
                "INVALID_STRING_CONTENT",
                exception.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(error);
    }

    @ExceptionHandler(TaskNotFound.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFound(
            TaskNotFound exception) {
        ErrorResponse error = new ErrorResponse(
                "TASK_NOT_FOUND",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }


    @ExceptionHandler(InvalidTaskStatusArg.class)
    public ResponseEntity<ErrorResponse> handleInvalidTaskStatusArg(
            InvalidTaskStatusArg exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "INVALID_TASK_STATUS_ARG",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "BAD_REQUEST",
                "Not valid or missing field: " + (exception.getFieldError().getField())
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleDefaultException(
            Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "EXCEPTION",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
}