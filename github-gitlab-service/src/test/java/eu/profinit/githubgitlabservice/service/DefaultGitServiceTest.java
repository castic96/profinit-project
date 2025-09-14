package eu.profinit.githubgitlabservice.service;

import eu.profinit.githubgitlabservice.client.GitApiClient;
import eu.profinit.githubgitlabservice.mapper.UserMapper;
import eu.profinit.githubgitlabservice.model.entity.Source;
import eu.profinit.githubgitlabservice.model.entity.User;
import eu.profinit.githubgitlabservice.model.entity.Username;
import eu.profinit.githubgitlabservice.model.enums.SourceEnum;
import eu.profinit.githubgitlabservice.model.internal.GitUserWithProjects;
import eu.profinit.githubgitlabservice.service.impl.DefaultGitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DefaultGitServiceTest {

    @Mock
    private DatabaseService databaseService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private GitApiClient gitApiClient;

    @InjectMocks
    private DefaultGitService defaultGitService;

    private Source source;
    private Username username;
    private User userEntity;
    private GitUserWithProjects gitUserWithProjects;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        source = new Source();
        source.setName(SourceEnum.GITHUB);

        username = new Username();
        username.setName("john-doe");

        userEntity = new User();
        userEntity.setLastUpdate(LocalDateTime.now());

        gitUserWithProjects = new GitUserWithProjects();
        gitUserWithProjects.setGitId(123L);

        Map<SourceEnum, GitApiClient> gitClients = Map.of(SourceEnum.GITHUB, gitApiClient);
        ReflectionTestUtils.setField(defaultGitService, "gitApiClients", gitClients);
        ReflectionTestUtils.setField(defaultGitService, "cacheTtlSeconds", 60);
    }

    @Test
    void shouldReturnEmptyWhenSourceIsInvalid() {
        Optional<GitUserWithProjects> result = defaultGitService.getUserForSource("john", SourceEnum.GITLAB);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenSourceNotFoundInDb() {
        when(databaseService.getSource(SourceEnum.GITHUB)).thenReturn(null);

        Optional<GitUserWithProjects> result = defaultGitService.getUserForSource("john", SourceEnum.GITHUB);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldFetchAndSaveWhenUserNotInDb() {
        when(databaseService.getSource(SourceEnum.GITHUB)).thenReturn(source);
        when(databaseService.getUsername("john-doe")).thenReturn(username);
        when(databaseService.getUser(username, source)).thenReturn(Optional.empty());
        when(gitApiClient.getUserWithProjects("john-doe")).thenReturn(Optional.of(gitUserWithProjects));
        when(databaseService.createNewUserEntity(username, source, 123L)).thenReturn(userEntity);

        Optional<GitUserWithProjects> result = defaultGitService.getUserForSource("john-doe", SourceEnum.GITHUB);

        assertThat(result).contains(gitUserWithProjects);
        verify(databaseService).saveUserWithProjects(gitUserWithProjects, userEntity);
    }

    @Test
    void shouldRefetchWhenCacheExpired() {
        userEntity.setLastUpdate(LocalDateTime.now().minusMinutes(10));

        when(databaseService.getSource(SourceEnum.GITHUB)).thenReturn(source);
        when(databaseService.getUsername("john-doe")).thenReturn(username);
        when(databaseService.getUser(username, source)).thenReturn(Optional.of(userEntity));
        when(gitApiClient.getUserWithProjects("john-doe")).thenReturn(Optional.of(gitUserWithProjects));

        Optional<GitUserWithProjects> result = defaultGitService.getUserForSource("john-doe", SourceEnum.GITHUB);

        assertThat(result).contains(gitUserWithProjects);
        verify(databaseService).saveUserWithProjects(gitUserWithProjects, userEntity);
    }

    @Test
    void shouldReturnMappedEntityWhenCacheValid() {
        when(databaseService.getSource(SourceEnum.GITHUB)).thenReturn(source);
        when(databaseService.getUsername("john-doe")).thenReturn(username);
        when(databaseService.getUser(username, source)).thenReturn(Optional.of(userEntity));

        GitUserWithProjects mapped = new GitUserWithProjects();
        when(userMapper.entityToInternal(userEntity)).thenReturn(mapped);

        Optional<GitUserWithProjects> result = defaultGitService.getUserForSource("john-doe", SourceEnum.GITHUB);

        assertThat(result).contains(mapped);
        verify(databaseService, never()).saveUserWithProjects(any(), any());
    }

}
