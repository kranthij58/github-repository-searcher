package com.kranthi.github_searcher.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString
public class GitHubRepositoryItem {

    private Long id;

    private String name;

    private String description;

    private GitHubOwner owner;

    @JsonProperty("html_url")
    private String htmlUrl;

    private String language;

    @JsonProperty("stargazers_count")
    private Integer stars;

    @JsonProperty("forks_count")
    private Integer forks;

    @JsonProperty("updated_at")
    private Instant lastUpdated;
}
