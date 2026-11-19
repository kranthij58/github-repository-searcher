package com.kranthi.github_searcher.service;

import com.kranthi.github_searcher.client.GitHubApiClient;
import com.kranthi.github_searcher.dto.*;
import com.kranthi.github_searcher.entity.GitHubRepository;
import com.kranthi.github_searcher.mapper.SearchResponseEntityMapper;
import com.kranthi.github_searcher.repository.GitHubRepositoryRepo;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public GitHubSearchApiResponse searchAndSaveRepositories(GitHubSearchRequest searchRequest) {

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
}
