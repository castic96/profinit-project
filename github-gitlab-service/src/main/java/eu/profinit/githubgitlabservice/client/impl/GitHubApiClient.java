package eu.profinit.githubgitlabservice.client.impl;

import eu.profinit.githubgitlabservice.client.GitApiClient;
import eu.profinit.githubgitlabservice.exception.GitClientException;
import eu.profinit.githubgitlabservice.mapper.UserMapper;
import eu.profinit.githubgitlabservice.model.dto.github.GitHubProjectResponseDto;
import eu.profinit.githubgitlabservice.model.dto.github.GitHubResponseDto;
import eu.profinit.githubgitlabservice.model.internal.GitUserWithProjects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class GitHubApiClient implements GitApiClient {

    private static final String GET_USER_URI = "/users/{username}";
    private static final String GET_USER_REPOS_URI = "/users/{username}/repos";

    private final WebClient webClient;
    private final UserMapper userMapper;

    @Override
    public Optional<GitUserWithProjects> getUserWithProjects(String username) {
        log.info("Getting user with projects for user with username '{}' from GitHub.", username);

        // 1) fetch user
        GitHubResponseDto user = getGitHubUser(username);

        if (user == null) {
            return Optional.empty();
        }

        // 2) fetch projects
        List<GitHubProjectResponseDto> projects = getGitHubProjects(username);
        user.setProjects(projects);

        // 3) map to an internal model
        return Optional.of(userMapper.gitHubUserToInternal(user));
    }

    private GitHubResponseDto getGitHubUser(String username) {
        log.info("Getting user with username '{}' from GitHub.", username);

        return webClient.get()
                .uri(GET_USER_URI, username)
                .retrieve()
                .onStatus(this::isErrorStatus, response -> checkStatus(response, username, "user"))
                .bodyToMono(GitHubResponseDto.class)
                .doOnNext(u -> log.info("Successfully fetched GitHub user: {}", username))
                .onErrorResume(ex -> {
                    log.error("Failed to deserialize GitHub user {}: {}", username, ex.getMessage());
                    return Mono.empty();
                })
                .block();
    }

    private List<GitHubProjectResponseDto> getGitHubProjects(String username) {
        log.info("Getting projects for user with username '{}' GitHub.", username);

        return webClient.get()
                .uri(GET_USER_REPOS_URI, username)
                .retrieve()
                .onStatus(this::isErrorStatus, response -> checkStatus(response, username, "repos"))
                .bodyToFlux(GitHubProjectResponseDto.class)
                .collectList()
                .doOnNext(u -> log.info(
                        "Successfully fetched GitHub projects for user: {}", username)
                )
                .onErrorResume(ex -> {
                    log.error("Failed to deserialize GitHub projects for user {}: {}", username, ex.getMessage());
                    return Mono.just(Collections.emptyList());
                })
                .blockOptional()
                .orElse(Collections.emptyList());
    }

    private boolean isErrorStatus(HttpStatusCode status) {
        return !status.is2xxSuccessful();
    }

    private Mono<Throwable> checkStatus(ClientResponse response, String username, String type) {
        HttpStatusCode statusCode = response.statusCode();

        if (statusCode == HttpStatus.NOT_FOUND) {
            log.warn("GitHub {} not found - username: {}", type, username);
            return Mono.error(new GitClientException(
                            "GitHub " + type + " not found.",
                            username,
                            statusCode.value()
                    )
            );
        } else {
            log.error("Unexpected HTTP status fetching GitHub {} - username: {}, status code: {}", type, username, statusCode);
            return Mono.error(
                    new GitClientException(
                            "Unexpected HTTP status fetching GitHub " + type,
                            username,
                            statusCode.value()
                    )
            );
        }
    }

}
