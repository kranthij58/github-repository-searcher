package com.kranthi.github_searcher.client;

import com.kranthi.github_searcher.dto.GitHubSearchRequest;
import com.kranthi.github_searcher.dto.GitHubSearchResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GitHubApiClient {

    private final RestClient restClient;

    public GitHubApiClient(RestClient restClient){
        this.restClient = restClient;
    }

    public GitHubSearchResponse getResultRepositories(GitHubSearchRequest request){
        String query = request.getQuery();

        if (request.getLanguage() != null
                && !request.getLanguage().isBlank()) {
            query += " language:" + request.getLanguage();
        }
        final String searchQuery = query;

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/repositories")
                        .queryParam("q", searchQuery)
                        .queryParam("sort", request.getSort())
                        .queryParam("order", "desc")
                        .queryParam("per_page", 100)
                        .queryParam("page", 1)
                        .build())
                .retrieve()
                .body(GitHubSearchResponse.class);
    }

}
