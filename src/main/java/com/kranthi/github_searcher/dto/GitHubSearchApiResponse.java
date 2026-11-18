
package com.kranthi.github_searcher.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class GitHubSearchApiResponse {

    private String message;
    private List<GitHubRepositoryResponse> repositories;

}
