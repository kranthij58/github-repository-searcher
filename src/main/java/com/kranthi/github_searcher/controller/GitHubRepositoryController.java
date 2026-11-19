package com.kranthi.github_searcher.controller;

import com.kranthi.github_searcher.dto.GitHubSearchApiResponse;
import com.kranthi.github_searcher.dto.GitHubSearchRequest;
import com.kranthi.github_searcher.dto.GitHubSearchResponse;
import com.kranthi.github_searcher.service.GitHubRepositoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/github")
public class GitHubRepositoryController {

    private final GitHubRepositoryService repositoryService;

    public GitHubRepositoryController(GitHubRepositoryService repositoryService){
        this.repositoryService = repositoryService;
    }

    @PostMapping("/search")
    public ResponseEntity<GitHubSearchApiResponse> searchRequest(@RequestBody GitHubSearchRequest searchRequest) {

        GitHubSearchApiResponse searchApiResponse = repositoryService.searchAndSaveRepositories(searchRequest);
        return ResponseEntity.ok(searchApiResponse);
    }

}
