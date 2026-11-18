package com.kranthi.github_searcher.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GitHubSearchRequest {

    private String query;
    private String language;
    private String sort;

}
