package com.kranthi.github_searcher.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

// Response received from GitHub after performing a repository search.
@Getter
@Setter
@ToString
public class GitHubSearchResponse {
    //GitHubApi client's response
    @JsonProperty("total_count")
    private Integer totalCount;

    @JsonProperty("incomplete_results")
    private Boolean incompleteResults;

    private List<GitHubRepositoryItem> items;
}
