package com.kranthi.github_searcher.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Request received from the client to search GitHub repositories.

@Getter
@Setter
@ToString
public class GitHubSearchRequest {

    @NotBlank(message = "Search query cannot be blank")
    private String query;

    private String language;
    private String sort;

}
