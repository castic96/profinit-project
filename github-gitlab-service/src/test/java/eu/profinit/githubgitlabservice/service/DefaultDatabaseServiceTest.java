package eu.profinit.githubgitlabservice.service;

import eu.profinit.githubgitlabservice.mapper.ProjectMapper;
import eu.profinit.githubgitlabservice.mapper.UserMapper;
import eu.profinit.githubgitlabservice.model.entity.Project;
import eu.profinit.githubgitlabservice.model.entity.Source;
import eu.profinit.githubgitlabservice.model.entity.User;
import eu.profinit.githubgitlabservice.model.entity.Username;
import eu.profinit.githubgitlabservice.model.enums.SourceEnum;
import eu.profinit.githubgitlabservice.model.internal.GitProject;
import eu.profinit.githubgitlabservice.model.internal.GitUserWithProjects;
import eu.profinit.githubgitlabservice.repository.SourceRepository;
import eu.profinit.githubgitlabservice.repository.UserRepository;
import eu.profinit.githubgitlabservice.repository.UsernameRepository;
import eu.profinit.githubgitlabservice.service.impl.DefaultDatabaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DefaultDatabaseServiceTest {

    @Mock
    private SourceRepository sourceRepository;

    @Mock
    private UsernameRepository usernameRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private DefaultDatabaseService databaseService;

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
        userEntity.setUsername(username);
        userEntity.setSource(source);
        userEntity.setProjects(new ArrayList<>());

        gitUserWithProjects = new GitUserWithProjects();
        gitUserWithProjects.setGitId(123L);
        GitProject project = new GitProject();
        project.setGitId(1L);
        gitUserWithProjects.setProjects(List.of(project));
    }

    @Test
    void shouldReturnSourceEntityWhenExists() {
        when(sourceRepository.findByName(SourceEnum.GITHUB)).thenReturn(Optional.of(source));

        Source result = databaseService.getSource(SourceEnum.GITHUB);

        assertThat(result).isEqualTo(source);
    }

    @Test
    void shouldReturnNullWhenSourceEntityNotFound() {
        when(sourceRepository.findByName(SourceEnum.GITHUB)).thenReturn(Optional.empty());

        Source result = databaseService.getSource(SourceEnum.GITHUB);

        assertThat(result).isNull();
    }

    @Test
    void shouldReturnUsernameEntityWhenExists() {
        when(usernameRepository.findByName("john-doe")).thenReturn(Optional.of(username));

        Username result = databaseService.getUsername("john-doe");

        assertThat(result).isEqualTo(username);
    }

    @Test
    void shouldCreateNewUsernameEntityWhenNotFound() {
        when(usernameRepository.findByName("john-doe")).thenReturn(Optional.empty());
        when(usernameRepository.save(any(Username.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Username result = databaseService.getUsername("john-doe");

        assertThat(result.getName()).isEqualTo("john-doe");
    }

    @Test
    void shouldReturnOptionalOfUserEntity() {
        when(userRepository.findByUsernameAndSource(username, source)).thenReturn(Optional.of(userEntity));

        Optional<User> result = databaseService.getUser(username, source);

        assertThat(result).contains(userEntity);
    }

    @Test
    void shouldCreateNewUserEntity() {
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = databaseService.createNewUserEntity(username, source, 123L);

        assertThat(result.getUsername()).isEqualTo(username);
        assertThat(result.getSource()).isEqualTo(source);
        assertThat(result.getGitId()).isEqualTo(123L);
    }

    @Test
    void shouldSaveUserAndProjects() {
        userEntity.setProjects(new ArrayList<>());

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        doAnswer(invocation -> {
            GitProject gitProject = invocation.getArgument(0);
            Project project = invocation.getArgument(1);
            project.setGitId(gitProject.getGitId());
            return null;
        }).when(projectMapper).internalToEntity(any(), any());

        databaseService.saveUserWithProjects(gitUserWithProjects, userEntity);

        verify(userMapper).internalToEntity(gitUserWithProjects, userEntity);

        verify(projectMapper).internalToEntity(any(), any());


        assertThat(userEntity.getProjects()).hasSize(1);
        assertThat(userEntity.getProjects().get(0).getGitId()).isEqualTo(1L);
    }

}
