package eu.profinit.githubgitlabservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "git")
public class GitApiClientProperties {

    private GitLab gitlab;
    private GitHub github;

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
