package eu.profinit.githubgitlabservice.client;

import eu.profinit.githubgitlabservice.model.internal.GitUserWithProjects;

import java.util.Optional;

public interface GitApiClient {
    Optional<GitUserWithProjects> getUserWithProjects(final String username);
}
