package eu.profinit.githubgitlabservice.config;

import eu.profinit.githubgitlabservice.client.GitApiClient;
import eu.profinit.githubgitlabservice.client.impl.GitHubApiClient;
import eu.profinit.githubgitlabservice.client.impl.GitLabServiceClient;
import eu.profinit.githubgitlabservice.mapper.UserMapper;
import eu.profinit.githubgitlabservice.model.enums.SourceEnum;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.EnumMap;
import java.util.Map;

@Configuration
public class GitApiClientConfig {

    @Bean("gitLabWebClient")
    public WebClient gitLabWebClient(WebClient.Builder builder, GitApiClientProperties gitApiClientProperties) {
        return builder
                .baseUrl(gitApiClientProperties.getGitlab().getBaseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, gitApiClientProperties.getGitlab().getAccept())
                .build();
    }

    @Bean("gitHubWebClient")
    public WebClient gitHubWebClient(WebClient.Builder builder, GitApiClientProperties gitApiClientProperties) {
        return builder
                .baseUrl(gitApiClientProperties.getGithub().getBaseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, gitApiClientProperties.getGithub().getAccept())
                .build();
    }

    @Primary
    @Bean("gitLabClient")
    public GitApiClient gitLabServiceClient(
            @Qualifier("gitLabWebClient") WebClient webClient,
            UserMapper userMapper
    ) {
        return new GitLabServiceClient(webClient, userMapper);
    }

    @Bean("gitHubClient")
    public GitApiClient gitHubApiClient(
            @Qualifier("gitHubWebClient") WebClient webClient,
            UserMapper userMapper
    ) {
        return new GitHubApiClient(webClient, userMapper);
    }

    @Bean
    public Map<SourceEnum, GitApiClient> gitApiClientsMap(
            @Qualifier("gitLabClient") GitApiClient gitLabServiceClient,
            @Qualifier("gitHubClient") GitApiClient gitHubApiClient
    ) {
        Map<SourceEnum, GitApiClient> map = new EnumMap<>(SourceEnum.class);
        map.put(SourceEnum.GITLAB, gitLabServiceClient);
        map.put(SourceEnum.GITHUB, gitHubApiClient);
        return map;
    }

}
