package com.kranthi.github_searcher.controller;

import com.kranthi.github_searcher.dto.GitHubSearchRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/github")
public class GitHubRepositoryController {

    @PostMapping("search")
    public ResponseEntity<?> searchRequest(
            @RequestBody GitHubSearchRequest searchRequest) {

        return ResponseEntity.ok("Controller is working");
    }

}
