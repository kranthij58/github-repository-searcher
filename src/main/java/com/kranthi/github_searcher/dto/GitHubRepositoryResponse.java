
package com.kranthi.github_searcher.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

// Repository details returned to the client.
@Getter
@Setter
@NoArgsConstructor
@ToString
public class GitHubRepositoryResponse {

    @Schema(example = "123")
    private Long id;

    @Schema(example = "spring framework")
    private String name;

    @Schema(example = "spring framework")
    private String description;

    @Schema(example = "spring project")
    private String owner;

    @Schema(example = "java")
    private String language;

    @Schema(example = "2300")
    private Integer stars;

    @Schema(example = "100")
    private Integer forks;

    @Schema(example = "2026-11-19T10:30:00Z")
    private Instant lastUpdated;

}
