package com.kranthi.github_searcher.controller;

import com.kranthi.github_searcher.dto.GitHubRepositoriesApiResponse;
import com.kranthi.github_searcher.dto.GitHubSearchApiResponse;
import com.kranthi.github_searcher.dto.GitHubSearchRequest;
import com.kranthi.github_searcher.dto.GitHubSearchResponse;
import com.kranthi.github_searcher.service.GitHubRepositoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "GitHub Repository API",
        description = "APIs to search and retrieve GitHub repositories")
@RestController
@RequestMapping("/api/github")
public class GitHubRepositoryController {

    private final GitHubRepositoryService repositoryService;

    public GitHubRepositoryController(GitHubRepositoryService repositoryService){
        this.repositoryService = repositoryService;
    }

    @Operation(
            summary = "Search GitHub repositories",
            description = "Searches GitHub repositories using a query and optional language filter, then saves the results in the database."
    )
    @PostMapping("/search")
    public ResponseEntity<GitHubSearchApiResponse> searchRequest(@Valid @RequestBody GitHubSearchRequest searchRequest) {

        GitHubSearchApiResponse searchApiResponse = repositoryService.searchAndSaveRepositories(searchRequest);
        return ResponseEntity.ok(searchApiResponse);
    }

    @Operation(
            summary = "Get saved repositories",
            description = "Retrieves repositories stored in the database with optional language, minimum stars and sorting filters."
    )
    @GetMapping("/repositories")
    public ResponseEntity<GitHubRepositoriesApiResponse> getRepositories(
            @Parameter(example = "Java")
            @RequestParam(required = false) String language,

            @Parameter(example = "100")
            @Min(value = 0, message = "Minimum stars cannot be negative")
            @RequestParam(required = false) Integer minStars,

            @Parameter(example = "stars")
            @RequestParam(defaultValue = "stars") String sort
    ) {
        GitHubRepositoriesApiResponse response =
                repositoryService.getRepositories(language, minStars, sort);

        return ResponseEntity.ok(response);
    }

}
