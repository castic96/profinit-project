package eu.profinit.githubgitlabservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "git")
@Getter
public class GitApiClientProperties {

    private final GitLab gitLab = new GitLab();
    private final GitHub gitHub = new GitHub();

    @Setter
    @Getter
    public static class GitLab {
        private String baseUrl;
        private String accept;
    }

    @Setter
    @Getter
    public static class GitHub {
        private String baseUrl;
        private String accept;
    }

}
