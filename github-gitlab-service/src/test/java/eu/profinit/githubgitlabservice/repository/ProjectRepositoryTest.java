package eu.profinit.githubgitlabservice.repository;

import eu.profinit.githubgitlabservice.model.entity.Project;
import eu.profinit.githubgitlabservice.model.entity.Source;
import eu.profinit.githubgitlabservice.model.entity.User;
import eu.profinit.githubgitlabservice.model.entity.Username;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsernameRepository usernameRepository;

    @Autowired
    private SourceRepository sourceRepository;

    @Test
    void shouldFindProjectsByUser() {
        String usernameStr = "username1";
        String sourceNameStr1 = "source1";
        String sourceNameStr2 = "source2";
        String projectNameStr11 = "Project 1_1";
        String projectNameStr21 = "Project 2_1";
        String projectNameStr12 = "Project 1_2";

        // Create Username entity
        Username username = new Username();
        username.setName(usernameStr);
        username = usernameRepository.save(username);

        // Create Source entity 1
        Source source1 = new Source();
        source1.setName(sourceNameStr1);
        source1 = sourceRepository.save(source1);

        // Create Source entity 2
        Source source2 = new Source();
        source2.setName(sourceNameStr2);
        source2 = sourceRepository.save(source2);

        // Create User entity 1
        User user1 = new User();
        user1.setUsername(username);
        user1.setSource(source1);
        user1 = userRepository.save(user1);

        // Create User entity 2
        User user2 = new User();
        user2.setUsername(username);
        user2.setSource(source2);
        user2 = userRepository.save(user2);

        // Create Project entity 1 for user1
        Project project11 = new Project();
        project11.setName(projectNameStr11);
        project11.setUser(user1);
        projectRepository.save(project11);

        // Create Project entity 2 for user1
        Project project21 = new Project();
        project21.setName(projectNameStr21);
        project21.setUser(user1);
        projectRepository.save(project21);

        // Create Project entity 1 for user2
        Project project12 = new Project();
        project12.setName(projectNameStr12);
        project12.setUser(user2);
        projectRepository.save(project12);

        // Fetch projects
        List<Project> projectsUser1 = projectRepository.findByUser(user1);
        List<Project> projectsUser2 = projectRepository.findByUser(user2);

        // Assertions
        assertThat(projectsUser1).hasSize(2);
        assertThat(projectsUser2).hasSize(1);
        assertTrue(projectsUser1.stream().allMatch(p -> p.getId() != null));
        assertTrue(projectsUser2.stream().allMatch(p -> p.getId() != null));
        assertThat(projectsUser1).extracting(Project::getName)
                .containsExactlyInAnyOrder(projectNameStr11, projectNameStr21);
        assertThat(projectsUser2).extracting(Project::getName)
                .containsExactly(projectNameStr12);

    }

}
