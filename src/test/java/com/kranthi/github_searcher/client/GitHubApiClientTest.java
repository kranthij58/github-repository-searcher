
package com.kranthi.github_searcher.client;

import com.kranthi.github_searcher.dto.GitHubSearchRequest;
import com.kranthi.github_searcher.dto.GitHubSearchResponse;
import com.kranthi.github_searcher.exception.GitHubApiException;
import com.kranthi.github_searcher.exception.GitHubRateLimitException;
import com.kranthi.github_searcher.exception.GitHubUnavailableException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GitHubApiClientTest {

    private MockWebServer mockWebServer;
    private GitHubApiClient gitHubApiClient;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        RestClient restClient = RestClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        gitHubApiClient = new GitHubApiClient(restClient);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    // Test successful GitHub API response
    @Test
    void testSuccessfulResponse() {

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody("""
                        {
                            "total_count": 1,
                            "items": []
                        }
                        """));

        GitHubSearchRequest request = new GitHubSearchRequest();
        request.setQuery("spring boot");
        request.setLanguage("Java");
        request.setSort("stars");

        GitHubSearchResponse response =
                gitHubApiClient.getResultRepositories(request);

        assertNotNull(response);
        assertEquals(1, response.getTotalCount());
    }

    // Test GitHub rate limit with 403 response
    @Test
    void testRateLimit403() {

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(403)
                .addHeader("X-RateLimit-Remaining", "0")
                .addHeader("X-RateLimit-Reset", "1800000000")
                .setBody("""
                        {
                            "message": "API rate limit exceeded"
                        }
                        """));

        GitHubSearchRequest request = new GitHubSearchRequest();
        request.setQuery("spring boot");

        assertThrows(
                GitHubRateLimitException.class,
                () -> gitHubApiClient.getResultRepositories(request)
        );
    }

    // Test GitHub rate limit with 429 response
    @Test
    void testRateLimit429() {

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(429)
                .addHeader("Retry-After", "60")
                .setBody("""
                        {
                            "message": "Too many requests"
                        }
                        """));

        GitHubSearchRequest request = new GitHubSearchRequest();
        request.setQuery("spring boot");

        assertThrows(
                GitHubRateLimitException.class,
                () -> gitHubApiClient.getResultRepositories(request)
        );
    }

    // Test GitHub server error
    @Test
    void testServerError() {

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500));

        GitHubSearchRequest request = new GitHubSearchRequest();
        request.setQuery("spring boot");

        assertThrows(
                GitHubApiException.class,
                () -> gitHubApiClient.getResultRepositories(request)
        );
    }

    // Test network connection failure
    @Test
    void testNetworkFailure() throws IOException {

        mockWebServer.shutdown();

        GitHubSearchRequest request = new GitHubSearchRequest();
        request.setQuery("spring boot");

        assertThrows(
                GitHubUnavailableException.class,
                () -> gitHubApiClient.getResultRepositories(request)
        );
    }

    // Test invalid JSON response
    @Test
    void testInvalidResponseBody() {

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody("invalid json"));

        GitHubSearchRequest request = new GitHubSearchRequest();
        request.setQuery("spring boot");

        assertThrows(
                GitHubApiException.class,
                () -> gitHubApiClient.getResultRepositories(request)
        );
    }
}
