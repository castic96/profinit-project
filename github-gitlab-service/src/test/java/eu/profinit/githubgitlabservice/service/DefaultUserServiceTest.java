package eu.profinit.githubgitlabservice.service;

import eu.profinit.githubgitlabservice.dto.UserResponse;
import eu.profinit.githubgitlabservice.mapper.UserMapper;
import eu.profinit.githubgitlabservice.model.enums.SourceEnum;
import eu.profinit.githubgitlabservice.model.internal.GitUserWithProjects;
import eu.profinit.githubgitlabservice.service.impl.DefaultUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DefaultUserServiceTest {

    @Mock
    private GitService gitService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private DefaultUserService userService;

    private GitUserWithProjects githubUser;
    private GitUserWithProjects gitlabUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        githubUser = new GitUserWithProjects();
        githubUser.setGitId(123L);

        gitlabUser = new GitUserWithProjects();
        gitlabUser.setGitId(456L);
    }

    @Test
    void shouldReturnMappedResponse() {
        Map<SourceEnum, Optional<GitUserWithProjects>> serviceResults = Map.of(
                SourceEnum.GITHUB, Optional.of(githubUser),
                SourceEnum.GITLAB, Optional.of(gitlabUser)
        );

        for (SourceEnum source : SourceEnum.values()) {
            when(gitService.getUserForSource("john", source))
                    .thenReturn(serviceResults.getOrDefault(source, Optional.empty()));
        }

        Map<SourceEnum, GitUserWithProjects> mappedUsers = new EnumMap<>(SourceEnum.class);
        mappedUsers.put(SourceEnum.GITHUB, githubUser);
        mappedUsers.put(SourceEnum.GITLAB, gitlabUser);

        UserResponse response = new UserResponse();
        when(userMapper.internalMapToUserResponse(mappedUsers)).thenReturn(response);

        UserResponse result = userService.getUser("john");

        assertThat(result).isEqualTo(response);

        for (SourceEnum source : SourceEnum.values()) {
            verify(gitService).getUserForSource("john", source);
        }

        verify(userMapper).internalMapToUserResponse(mappedUsers);
    }

    @Test
    void shouldHandleEmptyUsersGracefully() {
        for (SourceEnum source : SourceEnum.values()) {
            when(gitService.getUserForSource("john", source))
                    .thenReturn(Optional.empty());
        }

        Map<SourceEnum, GitUserWithProjects> emptyMap = new EnumMap<>(SourceEnum.class);
        UserResponse response = new UserResponse();
        when(userMapper.internalMapToUserResponse(emptyMap)).thenReturn(response);

        UserResponse result = userService.getUser("john");

        assertThat(result).isEqualTo(response);

        for (SourceEnum source : SourceEnum.values()) {
            verify(gitService).getUserForSource("john", source);
        }

        verify(userMapper).internalMapToUserResponse(emptyMap);
    }
}
