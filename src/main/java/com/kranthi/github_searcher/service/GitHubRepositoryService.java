package com.kranthi.github_searcher.service;

import com.kranthi.github_searcher.client.GitHubApiClient;
import com.kranthi.github_searcher.dto.GitHubSearchRequest;
import com.kranthi.github_searcher.dto.GitHubSearchResponse;
import com.kranthi.github_searcher.repository.GitHubRepositoryRepo;
import org.springframework.stereotype.Service;

@Service
public class GitHubRepositoryService {

    private final GitHubRepositoryRepo repositoryRepo;
    private final GitHubApiClient gitHubApiClient;

    public GitHubRepositoryService(GitHubRepositoryRepo repositoryRepo, GitHubApiClient gitHubApiClient){
        this.repositoryRepo = repositoryRepo;
        this.gitHubApiClient = gitHubApiClient;
    }
    public void searchAndSaveRepositories(GitHubSearchRequest searchRequest) {

        GitHubSearchResponse searchResponse =
                gitHubApiClient.getResultRepositories(searchRequest);
        System.out.println(searchRequest);
        System.out.println(searchResponse);
        // use mapper and change it
        //save to db


    }
}
