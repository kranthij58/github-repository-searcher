
package com.kranthi.github_searcher.mapper;

import com.kranthi.github_searcher.dto.GitHubRepositoryItem;
import com.kranthi.github_searcher.dto.GitHubRepositoryResponse;
import com.kranthi.github_searcher.entity.GitHubRepository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SearchResponseEntityMapperTest {

    private final SearchResponseEntityMapper mapper =
            new SearchResponseEntityMapper();

    // Test mapping GitHub item to entity
    @Test
    void testItemToEntity() {

        GitHubRepositoryItem item = new GitHubRepositoryItem();
        item.setId(123L);
        item.setName("spring-boot");
        item.setDescription("Spring Boot framework");
        item.setLanguage("Java");
        item.setStars(100);
        item.setForks(20);
        item.setHtmlUrl("https://github.com/spring-projects/spring-boot");

        GitHubRepository result = mapper.itemToEntityMapper(item);

        assertNotNull(result);
        assertEquals(123L, result.getId());
        assertEquals("spring-boot", result.getName());
        assertEquals("Spring Boot framework", result.getDescription());
        assertEquals("Java", result.getLanguage());
        assertEquals(100, result.getStars());
        assertEquals(20, result.getForks());
        assertEquals(
                "https://github.com/spring-projects/spring-boot",
                result.getHtmlUrl()
        );
    }

    // Test mapping when owner is null
    @Test
    void testNullOwner() {

        GitHubRepositoryItem item = new GitHubRepositoryItem();
        item.setId(123L);
        item.setOwner(null);

        GitHubRepository result = mapper.itemToEntityMapper(item);

        assertNotNull(result);
        assertNull(result.getOwner());
    }

    // Test mapping entity to response
    @Test
    void testEntityToResponse() {

        GitHubRepository entity = new GitHubRepository();
        entity.setId(123L);
        entity.setName("spring-boot");
        entity.setDescription("Spring Boot framework");
        entity.setOwner("spring-projects");
        entity.setLanguage("Java");
        entity.setStars(100);
        entity.setForks(20);

        GitHubRepositoryResponse result =
                mapper.entityToResponseMapper(entity);

        assertNotNull(result);
        assertEquals(123L, result.getId());
        assertEquals("spring-boot", result.getName());
        assertEquals("Spring Boot framework", result.getDescription());
        assertEquals("spring-projects", result.getOwner());
        assertEquals("Java", result.getLanguage());
        assertEquals(100, result.getStars());
        assertEquals(20, result.getForks());

    }

}
