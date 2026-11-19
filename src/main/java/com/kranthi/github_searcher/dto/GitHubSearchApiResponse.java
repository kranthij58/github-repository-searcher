
package com.kranthi.github_searcher.dto;

import lombok.*;

import java.util.List;

// the list of repositories returned by the POST endpoint after saving.
@Getter
@Setter
@AllArgsConstructor
@ToString
public class GitHubSearchApiResponse {

    private String message;
    private List<GitHubRepositoryResponse> repositories;

}
