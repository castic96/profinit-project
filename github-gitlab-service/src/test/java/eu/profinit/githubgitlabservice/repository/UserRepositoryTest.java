package eu.profinit.githubgitlabservice.repository;

import eu.profinit.githubgitlabservice.model.entity.User;
import eu.profinit.githubgitlabservice.model.entity.Username;
import eu.profinit.githubgitlabservice.model.entity.Source;
import eu.profinit.githubgitlabservice.model.enums.SourceEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsernameRepository usernameRepository;

    @Autowired
    private SourceRepository sourceRepository;

    @Test
    void shouldFindUserByUsernameAndSource() {
        String usernameStr = "username1";
        Source source = sourceRepository.findByName(SourceEnum.GITHUB)
                .orElseThrow(() -> new IllegalStateException("Source not found in DB"));

        Username username = new Username();
        username.setName(usernameStr);
        username = usernameRepository.save(username);

        User user = new User();
        user.setUsername(username);
        user.setSource(source);
        user.setGitId(123L);
        userRepository.save(user);

        Optional<User> result = userRepository.findByUsernameAndSource(username, source);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isNotNull();
        assertThat(result.get().getGitId()).isNotNull();
        assertThat(result.get().getUsername().getName()).isEqualTo(usernameStr);
        assertThat(result.get().getSource().getName()).isEqualTo(SourceEnum.GITHUB);
    }

    @Test
    void shouldReturnEmptyWhenUserNotExists() {
        Username username = new Username();
        username.setName("nonexistent");
        usernameRepository.save(username);

        Source source = sourceRepository.findByName(SourceEnum.GITHUB)
                .orElseThrow(() -> new IllegalStateException("Source not found in DB"));

        Optional<User> result = userRepository.findByUsernameAndSource(username, source);

        assertThat(result).isEmpty();
    }
}