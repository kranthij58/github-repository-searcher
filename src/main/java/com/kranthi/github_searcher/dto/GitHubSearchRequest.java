package com.kranthi.github_searcher.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "spring boot")
    private String query;

    @Schema(example = "java")
    private String language;

    @Schema(example = "stars")
    private String sort;

}
