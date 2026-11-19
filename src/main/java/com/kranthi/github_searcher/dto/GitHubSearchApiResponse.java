
package com.kranthi.github_searcher.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class GitHubSearchApiResponse {

    private String message;
    private List<GitHubRepositoryResponse> repositories;

}
