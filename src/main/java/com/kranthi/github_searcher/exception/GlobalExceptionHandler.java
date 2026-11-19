
package com.kranthi.github_searcher.exception;

import com.kranthi.github_searcher.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handles validation errors in the POST request body
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request){

        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Request validation failed",
                request.getRequestURI(),
                errors
        );

        logger.warn("Invalid request body: {}", errors);
        return ResponseEntity.badRequest().body(response);
    }

    // Handles GET parameter validation, such as negative minStars
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodValidationException(HandlerMethodValidationException ex, HttpServletRequest request){

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Request parameter validation failed",
                request.getRequestURI(),
                Map.of("minStars", "Minimum stars cannot be negative")
        );

        logger.warn("Invalid minStars value");
        return ResponseEntity.badRequest().body(response);
    }

    // Handles invalid parameter types, such as minStars=abc
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        String expectedType = ex.getRequiredType() != null
                ? ex.getRequiredType().getSimpleName()
                : "valid value";

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Invalid request parameter",
                request.getRequestURI(),
                Map.of(
                        ex.getName(),
                        "Invalid value. Expected type: " + expectedType
                )
        );

        logger.warn("Invalid value for parameter: {}", ex.getName());
        return ResponseEntity.badRequest().body(response);
    }

    // Handles invalid sort values from POST and GET
    @ExceptionHandler(InvalidSortException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidSortException(InvalidSortException ex, HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI(),
                Map.of("sort", ex.getMessage())
        );

        logger.warn("Invalid sort value: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    // Handles GitHub rate limit errors
    @ExceptionHandler(GitHubRateLimitException.class)
    public ResponseEntity<ApiErrorResponse> handleGitHubRateLimit(GitHubRateLimitException ex, HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.TOO_MANY_REQUESTS.value(),
                "Too Many Requests",
                ex.getMessage(),
                request.getRequestURI(),
                Map.of()
        );

        ResponseEntity.BodyBuilder builder =
                ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS);

        if (ex.getResetTime() != null) {
            builder.header("X-RateLimit-Reset", ex.getResetTime());
        }

        logger.warn("GitHub rate limit exceeded");
        return builder.body(response);
    }


    // Handles other GitHub API errors
    @ExceptionHandler(GitHubApiException.class)
    public ResponseEntity<ApiErrorResponse> handleGitHubApiException(GitHubApiException ex, HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_GATEWAY.value(),
                "Bad Gateway",
                ex.getMessage(),
                request.getRequestURI(),
                Map.of()
        );

        logger.error("GitHub API request failed", ex);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
    }


    // Handles GitHub connection failures
    @ExceptionHandler(GitHubUnavailableException.class)
    public ResponseEntity<ApiErrorResponse> handleGitHubUnavailable(GitHubUnavailableException ex, HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Service Unavailable",
                ex.getMessage(),
                request.getRequestURI(),
                Map.of()
        );

        logger.error("Cannot connect to GitHub", ex);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    // Handles DB conflicts
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                "Database integrity violation",
                request.getRequestURI(),
                Map.of()
        );

        logger.error("Database integrity violation", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // Handles DB connection failures
    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity<ApiErrorResponse> handleDatabaseConnectionFailure(
            DataAccessResourceFailureException ex,
            HttpServletRequest request) {


        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(),
                "Database is currently unavailable",
                request.getRequestURI(),
                Map.of()
        );

        logger.error("Database connection failed", ex);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    // Handles other DB failures
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiErrorResponse> handleDatabaseException(
            DataAccessException ex,
            HttpServletRequest request) {

        logger.error("Database operation failed", ex);

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Database operation failed",
                request.getRequestURI(),
                Map.of()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // Handles unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred",
                request.getRequestURI(),
                Map.of()
        );

        logger.error("Something went wrong", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
