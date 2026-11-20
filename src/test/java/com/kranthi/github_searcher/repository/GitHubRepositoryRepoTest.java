
package com.kranthi.github_searcher.repository;

import com.kranthi.github_searcher.entity.GitHubRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class GitHubRepositoryRepoTest {

    @Autowired
    private GitHubRepositoryRepo repository;

    // Test saving and finding a repository
    @Test
    void testSaveAndFind() {

        GitHubRepository repo = new GitHubRepository();
        repo.setId(101L);
        repo.setName("spring-boot");
        repo.setLanguage("Java");
        repo.setStars(500);
        repo.setForks(100);
        repo.setLastUpdated(Instant.now());

        repository.save(repo);

        GitHubRepository result =
                repository.findById(101L).orElse(null);

        assertNotNull(result);
        assertEquals("spring-boot", result.getName());
        assertEquals(500, result.getStars());
    }

    // Test filtering by language and minimum stars
    @Test
    void testFilters() {

        GitHubRepository javaRepo = new GitHubRepository();
        javaRepo.setId(102L);
        javaRepo.setName("java-project");
        javaRepo.setLanguage("Java");
        javaRepo.setStars(500);

        GitHubRepository pythonRepo = new GitHubRepository();
        pythonRepo.setId(103L);
        pythonRepo.setName("python-project");
        pythonRepo.setLanguage("Python");
        pythonRepo.setStars(1000);

        GitHubRepository lowStarRepo = new GitHubRepository();
        lowStarRepo.setId(104L);
        lowStarRepo.setName("small-java-project");
        lowStarRepo.setLanguage("Java");
        lowStarRepo.setStars(50);

        repository.saveAll(
                List.of(javaRepo, pythonRepo, lowStarRepo)
        );

        List<GitHubRepository> result =
                repository.findByFilters(
                        "java",
                        100,
                        Sort.by(Sort.Direction.DESC, "stars")
                );

        assertEquals(1, result.size());
        assertEquals("java-project", result.get(0).getName());
    }

    // Test sorting by stars
    @Test
    void testSortByStars() {

        GitHubRepository first = new GitHubRepository();
        first.setId(105L);
        first.setName("repo-one");
        first.setStars(100);

        GitHubRepository second = new GitHubRepository();
        second.setId(106L);
        second.setName("repo-two");
        second.setStars(500);

        repository.saveAll(List.of(first, second));

        List<GitHubRepository> result =
                repository.findByFilters(
                        null,
                        null,
                        Sort.by(Sort.Direction.DESC, "stars")
                );

        assertTrue(result.size() >= 2);
        assertTrue(
                result.get(0).getStars() >= result.get(1).getStars()
        );
    }

    // Test DB upsert behavior update if existed, add if it doesn't
    @Test
    void testUpdateExistingRepository() {

        GitHubRepository first = new GitHubRepository();
        first.setId(123L);
        first.setName("spring-project");
        first.setStars(100);

        repository.saveAndFlush(first);

        GitHubRepository updated = new GitHubRepository();
        updated.setId(123L);
        updated.setName("spring-project-updated");
        updated.setStars(200);

        repository.saveAndFlush(updated);

        List<GitHubRepository> repositories = repository.findAll();

        assertEquals(1, repositories.size());
        assertEquals("spring-project-updated", repositories.get(0).getName());
        assertEquals(200, repositories.get(0).getStars());
    }


}
