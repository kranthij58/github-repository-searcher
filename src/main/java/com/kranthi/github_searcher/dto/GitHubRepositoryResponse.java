
package com.kranthi.github_searcher.dto;

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

    private Long id;
    private String name;
    private String description;
    private String owner;
    private String language;
    private Integer stars;
    private Integer forks;
    private Instant lastUpdated;

}
