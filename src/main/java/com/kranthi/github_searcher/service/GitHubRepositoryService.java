package com.kranthi.github_searcher.service;

import com.kranthi.github_searcher.client.GitHubApiClient;
import com.kranthi.github_searcher.dto.*;
import com.kranthi.github_searcher.entity.GitHubRepository;
import com.kranthi.github_searcher.exception.InvalidSortException;
import com.kranthi.github_searcher.mapper.SearchResponseEntityMapper;
import com.kranthi.github_searcher.repository.GitHubRepositoryRepo;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class GitHubRepositoryService {

    private final GitHubRepositoryRepo repositoryRepo;
    private final GitHubApiClient gitHubApiClient;
    private final SearchResponseEntityMapper responseEntityMapper;

    public GitHubRepositoryService(GitHubRepositoryRepo repositoryRepo, GitHubApiClient gitHubApiClient, SearchResponseEntityMapper responseEntityMapper){
        this.repositoryRepo = repositoryRepo;
        this.gitHubApiClient = gitHubApiClient;
        this.responseEntityMapper = responseEntityMapper;

    }

    private void validateSort(String sort) {

        if (sort != null &&
                !sort.equalsIgnoreCase("stars") &&
                !sort.equalsIgnoreCase("forks") &&
                !sort.equalsIgnoreCase("updated")) {

            throw new InvalidSortException(
                    "Invalid sort parameter. Allowed values: stars, forks, updated"
            );
        }
    }

    public GitHubSearchApiResponse searchAndSaveRepositories(GitHubSearchRequest searchRequest) {

        validateSort(searchRequest.getSort());
        GitHubSearchResponse searchResponse = gitHubApiClient.getResultRepositories(searchRequest);

        if (searchResponse == null || searchResponse.getItems() == null) {
            throw new IllegalStateException(
                    "Invalid response received from GitHub API"
            );
        }

        // use mapper and change it
        List<GitHubRepository> entities = searchResponse.getItems()
                .stream()
                .map(responseEntityMapper::itemToEntityMapper)
                .toList();


        List<GitHubRepository> savedEntities = repositoryRepo.saveAll(entities);

        List<GitHubRepositoryResponse> responseItems = savedEntities
                .stream()
                .map(responseEntityMapper::entityToResponseMapper)
                .toList();

        return new GitHubSearchApiResponse(
                "Repositories fetched and saved successfully",
                responseItems
        );


    }

    public GitHubRepositoriesApiResponse getRepositories(String language, Integer minStars, String sort) {

        if (language != null && language.isBlank()) {
            language = null;
        }else if (language != null) {
            language = language.toLowerCase(Locale.ROOT);
        }

        validateSort(sort);

        String sortField = "stars";

        if ("forks".equalsIgnoreCase(sort)) {
            sortField = "forks";
        } else if ("updated".equalsIgnoreCase(sort)) {
            sortField = "lastUpdated";
        }

        Sort sorting = Sort.by(Sort.Direction.DESC, sortField);

        List<GitHubRepository> repositories =
                repositoryRepo.findByFilters(language, minStars, sorting);

        List<GitHubRepositoryResponse> response = repositories.stream()
                .map(responseEntityMapper::entityToResponseMapper)
                .toList();

        return new GitHubRepositoriesApiResponse(response);
    }
}
