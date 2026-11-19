package com.kranthi.github_searcher.client;

import com.kranthi.github_searcher.dto.GitHubSearchRequest;
import com.kranthi.github_searcher.dto.GitHubSearchResponse;
import com.kranthi.github_searcher.exception.GitHubApiException;
import com.kranthi.github_searcher.exception.GitHubRateLimitException;
import com.kranthi.github_searcher.exception.GitHubUnavailableException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

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

        try{
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search/repositories")
                            .queryParam("q", searchQuery)
                            .queryParam("sort", request.getSort())
                            .queryParam("order", "desc")
                            .build())
                    .retrieve()
                    .body(GitHubSearchResponse.class);
        }catch (HttpClientErrorException ex) {

            HttpHeaders headers = ex.getResponseHeaders();

            String remaining = headers != null
                    ? headers.getFirst("X-RateLimit-Remaining")
                    : null;

            String resetTime = headers != null
                    ? headers.getFirst("X-RateLimit-Reset")
                    : null;

            String retryAfter = headers != null
                    ? headers.getFirst("Retry-After")
                    : null;

            String responseBody = ex.getResponseBodyAsString();

            boolean rateLimited =
                    ex.getStatusCode().value() == 429 ||
                            (ex.getStatusCode() == HttpStatus.FORBIDDEN &&
                                    ("0".equals(remaining) ||
                                            retryAfter != null ||
                                            responseBody.toLowerCase().contains("rate limit")));

            if (rateLimited) {
                throw new GitHubRateLimitException(
                        "GitHub API rate limit exceeded. Please try again later.",
                        resetTime
                );
            }

            throw new GitHubApiException(
                    "GitHub API rejected the request.",
                    ex
            );

        } catch (HttpServerErrorException ex) {

            throw new GitHubApiException(
                    "GitHub API is experiencing server errors.",
                    ex
            );

        } catch (ResourceAccessException ex) {

            throw new GitHubUnavailableException(
                    "Unable to connect to GitHub API.",
                    ex
            );

        } catch (RestClientException ex) {

            throw new GitHubApiException(
                    "Unable to process the GitHub API response.",
                    ex
            );
        }

    }

}
