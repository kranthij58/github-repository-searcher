package com.kranthi.github_searcher.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "github_repositories")
public class GitHubRepository {

    @Id
    @Column(name = "github_id")
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String owner;

    private String htmlUrl;

    private String language;

    private Integer stars;

    private Integer forks;

    private Instant lastUpdated;

}
