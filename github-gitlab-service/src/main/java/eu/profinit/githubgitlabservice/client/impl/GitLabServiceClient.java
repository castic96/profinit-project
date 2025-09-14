package eu.profinit.githubgitlabservice.client.impl;

import eu.profinit.githubgitlabservice.client.GitApiClient;
import eu.profinit.githubgitlabservice.exception.GitClientException;
import eu.profinit.githubgitlabservice.mapper.UserMapper;
import eu.profinit.githubgitlabservice.model.dto.gitlab.GitLabResponseDto;
import eu.profinit.githubgitlabservice.model.internal.GitUserWithProjects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Client for interacting with the provided GitLab Service.
 */
@Slf4j
@RequiredArgsConstructor
public class GitLabServiceClient implements GitApiClient {

    private static final String GET_USER_WITH_PROJECTS_URI = "/users/{username}";

    private final WebClient webClient;
    private final UserMapper userMapper;

    @Override
    public Optional<GitUserWithProjects> getUserWithProjects(String username) {
        log.info("Trying to get user with projects for user with username '{}' from GitLab.", username);

        // Fetch user with projects
        GitLabResponseDto user = getGitLabUserWithProjects(username);

        if (user == null) {
            return Optional.empty();
        }

        // Map to an internal model
        return Optional.of(userMapper.gitLabUserToInternal(user));
    }

    private GitLabResponseDto getGitLabUserWithProjects(String username) {
        log.debug("Getting user with projects with username '{}' from GitLab.", username);

        return webClient.get()
                .uri(GET_USER_WITH_PROJECTS_URI, username)
                .retrieve()
                .onStatus(this::isErrorStatus, response -> checkStatus(response, username))
                .bodyToMono(GitLabResponseDto.class)
                .doOnNext(u -> log.info("Successfully fetched GitLab user with projects: {}", username))
                .onErrorResume(ex -> {
                    log.error("Failed to deserialize GitLab user with projects {}: {}", username, ex.getMessage());
                    return Mono.empty();
                })
                .block();
    }

    private boolean isErrorStatus(HttpStatusCode status) {
        return !status.is2xxSuccessful();
    }

    private Mono<Throwable> checkStatus(ClientResponse response, String username) {
        HttpStatusCode statusCode = response.statusCode();

        if (statusCode == HttpStatus.NOT_FOUND) {
            log.warn("GitLab user with projects not found: {}", username);
            return Mono.error(
                    new GitClientException(
                            "GitLab user with projects not found.",
                            username,
                            statusCode.value()
                    )
            );
        } else {
            log.error("Unexpected HTTP status fetching GitLab user with projects {}: {}", username, statusCode);
            return Mono.error(
                    new GitClientException(
                            "Unexpected HTTP status fetching GitLab user with projects.",
                            username,
                            statusCode.value()
                    )
            );
        }
    }
}
