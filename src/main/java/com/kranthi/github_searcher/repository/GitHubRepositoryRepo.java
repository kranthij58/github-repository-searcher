package com.kranthi.github_searcher.repository;

import com.kranthi.github_searcher.entity.GitHubRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GitHubRepositoryRepo extends JpaRepository<GitHubRepository,Long> {

}
