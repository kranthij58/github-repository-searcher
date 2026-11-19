package com.kranthi.github_searcher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// Wraps the list of repositories returned by the GET endpoint.
@Getter
@Setter
@AllArgsConstructor
public class GitHubRepositoriesApiResponse {

    private List<GitHubRepositoryResponse> repositories;
}