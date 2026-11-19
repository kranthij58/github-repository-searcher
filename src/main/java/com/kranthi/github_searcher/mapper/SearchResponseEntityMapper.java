package com.kranthi.github_searcher.mapper;

import com.kranthi.github_searcher.dto.GitHubRepositoryItem;
import com.kranthi.github_searcher.dto.GitHubRepositoryResponse;
import com.kranthi.github_searcher.entity.GitHubRepository;
import org.springframework.stereotype.Component;

@Component
public class SearchResponseEntityMapper {

    public GitHubRepository itemToEntityMapper(GitHubRepositoryItem item) {

        GitHubRepository entity = new GitHubRepository();

        entity.setId(item.getId());
        entity.setName(item.getName());
        entity.setDescription(item.getDescription());

        entity.setOwner(
                item.getOwner() != null ? item.getOwner().getLogin() : null
        );

        entity.setLanguage(item.getLanguage());
        entity.setStars(item.getStars());
        entity.setHtmlUrl(item.getHtmlUrl());
        entity.setForks(item.getForks());
        entity.setLastUpdated(item.getLastUpdated());

        return entity;
    }

    public GitHubRepositoryResponse entityToResponseMapper(
            GitHubRepository entity) {

        GitHubRepositoryResponse response = new GitHubRepositoryResponse();

        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setOwner(entity.getOwner());
        response.setLanguage(entity.getLanguage());
        response.setStars(entity.getStars());
        response.setForks(entity.getForks());
        response.setLastUpdated(entity.getLastUpdated());

        return response;
    }
}