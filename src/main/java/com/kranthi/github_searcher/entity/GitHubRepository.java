package com.kranthi.github_searcher.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "github_repositories")
public class GitHubRepository {

    @Id
    @Column(name = "github_id")
    private Long id;
    private String name;
    private String description;
    private String owner;
    private String htmlUrl;
    private String language;
    private Integer stars;
    private Integer forks;
    private Instant lastUpdated;

}
