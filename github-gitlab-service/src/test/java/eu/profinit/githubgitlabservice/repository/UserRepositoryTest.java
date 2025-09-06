package eu.profinit.githubgitlabservice.repository;

import eu.profinit.githubgitlabservice.model.entity.Source;
import eu.profinit.githubgitlabservice.model.entity.User;
import eu.profinit.githubgitlabservice.model.entity.Username;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsernameRepository usernameRepository;

    @Autowired
    private SourceRepository sourceRepository;

    @Test
    void shouldFindUserByUsername() {
        String usernameStr = "username1";
        String sourceNameStr = "source1";

        // Create Username entity
        Username username = new Username();
        username.setName(usernameStr);
        username = usernameRepository.save(username);

        // Create Source entity
        Source source = new Source();
        source.setName(sourceNameStr);
        source = sourceRepository.save(source);

        // Create User entity
        User user = new User();
        user.setUsername(username);
        user.setSource(source);
        userRepository.save(user);

        // Fetch users by username
        List<User> users = userRepository.findByUsername(username);

        // Assertions
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getId()).isNotNull();
        assertThat(users.get(0).getUsername().getName()).isEqualTo(usernameStr);
        assertThat(users.get(0).getSource().getName()).isEqualTo(sourceNameStr);

    }

}
