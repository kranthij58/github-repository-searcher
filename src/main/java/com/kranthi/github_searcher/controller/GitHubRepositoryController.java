package com.kranthi.github_searcher.controller;

import com.kranthi.github_searcher.dto.GitHubRepositoriesApiResponse;
import com.kranthi.github_searcher.dto.GitHubSearchApiResponse;
import com.kranthi.github_searcher.dto.GitHubSearchRequest;
import com.kranthi.github_searcher.dto.GitHubSearchResponse;
import com.kranthi.github_searcher.service.GitHubRepositoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/github")
public class GitHubRepositoryController {

    private final GitHubRepositoryService repositoryService;

    public GitHubRepositoryController(GitHubRepositoryService repositoryService){
        this.repositoryService = repositoryService;
    }

    @PostMapping("/search")
    public ResponseEntity<GitHubSearchApiResponse> searchRequest(@Valid @RequestBody GitHubSearchRequest searchRequest) {

        GitHubSearchApiResponse searchApiResponse = repositoryService.searchAndSaveRepositories(searchRequest);
        return ResponseEntity.ok(searchApiResponse);
    }

    @GetMapping("/repositories")
    public ResponseEntity<GitHubRepositoriesApiResponse> getRepositories(
            @RequestParam(required = false) String language,
            @Min(value = 0, message = "Minimum stars cannot be negative")
            @RequestParam(required = false) Integer minStars,
            @RequestParam(defaultValue = "stars") String sort
    ) {
        GitHubRepositoriesApiResponse response =
                repositoryService.getRepositories(language, minStars, sort);

        return ResponseEntity.ok(response);
    }

}
