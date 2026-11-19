
package com.kranthi.github_searcher.service;

import com.kranthi.github_searcher.client.GitHubApiClient;
import com.kranthi.github_searcher.dto.*;
import com.kranthi.github_searcher.entity.GitHubRepository;
import com.kranthi.github_searcher.exception.InvalidSortException;
import com.kranthi.github_searcher.mapper.SearchResponseEntityMapper;
import com.kranthi.github_searcher.repository.GitHubRepositoryRepo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GitHubRepositoryServiceTest {

    @Mock
    private GitHubRepositoryRepo repositoryRepo;

    @Mock
    private GitHubApiClient gitHubApiClient;

    @Mock
    private SearchResponseEntityMapper responseEntityMapper;

    @InjectMocks
    private GitHubRepositoryService service;

    // Test search and save
    @Test
    void testSearchAndSave() {

        GitHubSearchRequest request = mock(GitHubSearchRequest.class);
        when(request.getSort()).thenReturn("stars");

        GitHubSearchResponse githubResponse = mock(GitHubSearchResponse.class);
        GitHubRepositoryItem item = mock(GitHubRepositoryItem.class);

        when(gitHubApiClient.getResultRepositories(request))
                .thenReturn(githubResponse);

        when(githubResponse.getItems())
                .thenReturn(List.of(item));

        GitHubRepository entity = new GitHubRepository();
        entity.setId(123L);
        entity.setName("spring-boot");

        when(responseEntityMapper.itemToEntityMapper(item))
                .thenReturn(entity);

        when(repositoryRepo.saveAll(anyList()))
                .thenReturn(List.of(entity));

        when(responseEntityMapper.entityToResponseMapper(entity))
                .thenReturn(new GitHubRepositoryResponse());

        GitHubSearchApiResponse result =
                service.searchAndSaveRepositories(request);

        assertNotNull(result);

        verify(gitHubApiClient).getResultRepositories(request);
        verify(repositoryRepo).saveAll(anyList());
        verify(responseEntityMapper).itemToEntityMapper(item);
        verify(responseEntityMapper).entityToResponseMapper(entity);
    }

    // Test when GitHub returns no results
    @Test
    void testEmptyResults() {

        GitHubSearchRequest request = mock(GitHubSearchRequest.class);
        when(request.getSort()).thenReturn("stars");

        GitHubSearchResponse githubResponse = mock(GitHubSearchResponse.class);

        when(gitHubApiClient.getResultRepositories(request))
                .thenReturn(githubResponse);

        when(githubResponse.getItems())
                .thenReturn(List.of());

        when(repositoryRepo.saveAll(anyList()))
                .thenReturn(List.of());

        GitHubSearchApiResponse result =
                service.searchAndSaveRepositories(request);

        assertNotNull(result);

        verify(repositoryRepo).saveAll(anyList());
        verify(responseEntityMapper, never())
                .itemToEntityMapper(any());
    }

    // Test when GitHub response is null
    @Test
    void testNullResponse() {

        GitHubSearchRequest request = mock(GitHubSearchRequest.class);
        when(request.getSort()).thenReturn("stars");

        when(gitHubApiClient.getResultRepositories(request))
                .thenReturn(null);

        assertThrows(
                IllegalStateException.class,
                () -> service.searchAndSaveRepositories(request)
        );

        verify(repositoryRepo, never()).saveAll(anyList());
    }

    // Test when response items are null
    @Test
    void testNullItems() {

        GitHubSearchRequest request = mock(GitHubSearchRequest.class);
        when(request.getSort()).thenReturn("stars");

        GitHubSearchResponse githubResponse = mock(GitHubSearchResponse.class);

        when(gitHubApiClient.getResultRepositories(request))
                .thenReturn(githubResponse);

        when(githubResponse.getItems()).thenReturn(null);

        assertThrows(
                IllegalStateException.class,
                () -> service.searchAndSaveRepositories(request)
        );

        verify(repositoryRepo, never()).saveAll(anyList());
    }

    // Test invalid sort value
    @Test
    void testInvalidSearchSort() {

        GitHubSearchRequest request = mock(GitHubSearchRequest.class);
        when(request.getSort()).thenReturn("invalid");

        assertThrows(
                InvalidSortException.class,
                () -> service.searchAndSaveRepositories(request)
        );

        verifyNoInteractions(gitHubApiClient);
        verifyNoInteractions(repositoryRepo);
    }

    // Test language filter
    @Test
    void testLanguageFilter() {

        when(repositoryRepo.findByFilters(
                eq("java"),
                eq(100),
                any(Sort.class)
        )).thenReturn(List.of());

        GitHubRepositoriesApiResponse result =
                service.getRepositories("Java", 100, "stars");

        assertNotNull(result);

        verify(repositoryRepo).findByFilters(
                eq("java"),
                eq(100),
                any(Sort.class)
        );
    }

    // Test blank language
    @Test
    void testBlankLanguage() {

        when(repositoryRepo.findByFilters(
                isNull(),
                isNull(),
                any(Sort.class)
        )).thenReturn(List.of());

        service.getRepositories("   ", null, "stars");

        verify(repositoryRepo).findByFilters(
                isNull(),
                isNull(),
                any(Sort.class)
        );
    }

    // Test sorting by forks
    @Test
    void testSortByForks() {

        when(repositoryRepo.findByFilters(
                isNull(),
                isNull(),
                any(Sort.class)
        )).thenReturn(List.of());

        service.getRepositories(null, null, "forks");

        ArgumentCaptor<Sort> sortCaptor =
                ArgumentCaptor.forClass(Sort.class);

        verify(repositoryRepo).findByFilters(
                isNull(),
                isNull(),
                sortCaptor.capture()
        );

        Sort sort = sortCaptor.getValue();

        assertEquals(
                Sort.Direction.DESC,
                sort.getOrderFor("forks").getDirection()
        );
    }

    // Test sorting by updated date
    @Test
    void testSortByUpdated() {

        when(repositoryRepo.findByFilters(
                isNull(),
                isNull(),
                any(Sort.class)
        )).thenReturn(List.of());

        service.getRepositories(null, null, "updated");

        ArgumentCaptor<Sort> sortCaptor =
                ArgumentCaptor.forClass(Sort.class);

        verify(repositoryRepo).findByFilters(
                isNull(),
                isNull(),
                sortCaptor.capture()
        );

        Sort sort = sortCaptor.getValue();

        assertEquals(
                Sort.Direction.DESC,
                sort.getOrderFor("lastUpdated").getDirection()
        );
    }

    // Test invalid sort in GET
    @Test
    void testInvalidGetSort() {

        assertThrows(
                InvalidSortException.class,
                () -> service.getRepositories(null, null, "invalid")
        );

        verifyNoInteractions(repositoryRepo);
    }
}
