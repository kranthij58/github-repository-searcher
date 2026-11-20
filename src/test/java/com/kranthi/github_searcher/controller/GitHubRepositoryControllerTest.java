
package com.kranthi.github_searcher.controller;

import com.kranthi.github_searcher.dto.GitHubSearchApiResponse;
import com.kranthi.github_searcher.dto.GitHubRepositoriesApiResponse;
import com.kranthi.github_searcher.exception.InvalidSortException;
import com.kranthi.github_searcher.service.GitHubRepositoryService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@WebMvcTest(GitHubRepositoryController.class)
class GitHubRepositoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GitHubRepositoryService repositoryService;

    // Test successful search request
    @Test
    void testSearch() throws Exception {

        when(repositoryService.searchAndSaveRepositories(any()))
                .thenReturn(new GitHubSearchApiResponse(
                        "Repositories fetched and saved successfully",
                        List.of()
                ));

        mockMvc.perform(post("/api/github/search")
                        .contentType("application/json")
                        .content("""
                                {
                                    "query": "spring boot",
                                    "language": "Java",
                                    "sort": "stars"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Repositories fetched and saved successfully"))
                .andExpect(jsonPath("$.repositories").isArray());

        verify(repositoryService).searchAndSaveRepositories(any());
    }

    // Test when no repositories are found
    @Test
    void testEmptySearchResults() throws Exception {

        when(repositoryService.searchAndSaveRepositories(any()))
                .thenReturn(new GitHubSearchApiResponse(
                        "No repositories found",
                        List.of()
                ));

        mockMvc.perform(post("/api/github/search")
                        .contentType("application/json")
                        .content("""
                            {
                                "query": "nonexistent-project",
                                "language": "Java",
                                "sort": "stars"
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("No repositories found"))
                .andExpect(jsonPath("$.repositories").isEmpty());
    }

    // Test getting repositories
    @Test
    void testGetRepositories() throws Exception {

        when(repositoryService.getRepositories("Java", 100, "stars"))
                .thenReturn(new GitHubRepositoriesApiResponse(List.of()));

        mockMvc.perform(get("/api/github/repositories")
                        .param("language", "Java")
                        .param("minStars", "100")
                        .param("sort", "stars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.repositories").isArray());

        verify(repositoryService)
                .getRepositories("Java", 100, "stars");
    }

    // Test blank search query
    @Test
    void testBlankQuery() throws Exception {

        mockMvc.perform(post("/api/github/search")
                        .contentType("application/json")
                        .content("""
                                {
                                    "query": " ",
                                    "language": "Java",
                                    "sort": "stars"
                                }
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(repositoryService);
    }

    // Test negative minimum stars
    @Test
    void testNegativeMinStars() throws Exception {

        mockMvc.perform(get("/api/github/repositories")
                        .param("minStars", "-10"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(repositoryService);
    }

    // Test invalid sort value
    @Test
    void testInvalidSort() throws Exception {

        when(repositoryService.getRepositories(null, null, "invalid"))
                .thenThrow(new InvalidSortException(
                        "Invalid sort parameter. Allowed values: stars, forks, updated"
                ));

        mockMvc.perform(get("/api/github/repositories")
                        .param("sort", "invalid"))
                .andExpect(status().isBadRequest());

        verify(repositoryService)
                .getRepositories(null, null, "invalid");
    }
}
