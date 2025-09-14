package eu.profinit.githubgitlabservice.client;

import eu.profinit.githubgitlabservice.model.internal.GitUserWithProjects;

import java.util.Optional;

/**
 * Client for interacting with Git-based services like GitHub or GitLab.
 */
public interface GitApiClient {

    /**
     * Gets user with Git projects.
     *
     * @param username username of the user
     * @return an Optional containing the user with projects if found, or empty if not found
     */
    Optional<GitUserWithProjects> getUserWithProjects(final String username);
}
