
package com.kranthi.github_searcher.exception;

import com.kranthi.github_searcher.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    // Testing GitHub rate limit exception
    @Test
    void testGitHubRateLimitException() {

        GitHubRateLimitException exception =
                new GitHubRateLimitException(
                        "Rate limit exceeded", "12345");

        HttpServletRequest request = mock(HttpServletRequest.class);

        ResponseEntity<ApiErrorResponse> response =
                handler.handleGitHubRateLimit(exception, request);

        assertEquals(429, response.getStatusCode().value());
        assertEquals("12345",
                response.getHeaders().getFirst("X-RateLimit-Reset"));
        assertEquals("Rate limit exceeded",
                response.getBody().getMessage());
    }

    // Testing GitHub API exception
    @Test
    void testGitHubApiException() {

        GitHubApiException exception =
                new GitHubApiException("GitHub API failed", null);

        HttpServletRequest request = mock(HttpServletRequest.class);

        ResponseEntity<ApiErrorResponse> response =
                handler.handleGitHubApiException(exception, request);

        assertEquals(502, response.getStatusCode().value());
        assertEquals("GitHub API failed", response.getBody().getMessage());
    }

    // Testing GitHub connection failure
    @Test
    void testGitHubUnavailableException() {

        GitHubUnavailableException exception =
                new GitHubUnavailableException(
                        "Unable to connect to GitHub", null);

        HttpServletRequest request = mock(HttpServletRequest.class);

        ResponseEntity<ApiErrorResponse> response =
                handler.handleGitHubUnavailable(exception, request);

        assertEquals(503, response.getStatusCode().value());
        assertEquals("Unable to connect to GitHub",
                response.getBody().getMessage());
    }

    // Testing invalid sort exception
    @Test
    void testInvalidSortException() {

        InvalidSortException exception =
                new InvalidSortException("Invalid sort value");

        HttpServletRequest request = mock(HttpServletRequest.class);

        ResponseEntity<ApiErrorResponse> response =
                handler.handleInvalidSortException(exception, request);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Invalid sort value", response.getBody().getMessage());
        assertTrue(response.getBody().getValidationErrors().containsKey("sort"));
    }

    // Testing database integrity violation
    @Test
    void testDataIntegrityViolationException() {

        DataIntegrityViolationException exception =
                new DataIntegrityViolationException("Duplicate record");

        HttpServletRequest request = mock(HttpServletRequest.class);

        ResponseEntity<ApiErrorResponse> response =
                handler.handleDataIntegrityViolation(exception, request);

        assertEquals(409, response.getStatusCode().value());
        assertEquals("Database integrity violation",
                response.getBody().getMessage());
    }

    // Testing unexpected exception
    @Test
    void testGeneralException() {

        Exception exception = new RuntimeException("Unexpected error");

        HttpServletRequest request = mock(HttpServletRequest.class);

        ResponseEntity<ApiErrorResponse> response =
                handler.handleGeneralException(exception, request);

        assertEquals(500, response.getStatusCode().value());
        assertEquals("An unexpected error occurred",
                response.getBody().getMessage());
    }
}
