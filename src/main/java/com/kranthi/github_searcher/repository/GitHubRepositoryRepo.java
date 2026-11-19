package com.kranthi.github_searcher.repository;

import com.kranthi.github_searcher.entity.GitHubRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GitHubRepositoryRepo extends JpaRepository<GitHubRepository,Long> {

    @Query("""

            SELECT r FROM GitHubRepository r
            WHERE (:language IS NULL OR LOWER(r.language) = :language)
            AND (:minStars IS NULL OR r.stars >= :minStars)
            """)
    List<GitHubRepository> findByFilters(
            @Param("language") String language,
            @Param("minStars") Integer minStars,
            Sort sort
    );
}


