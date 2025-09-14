package eu.profinit.githubgitlabservice.service;

import eu.profinit.githubgitlabservice.model.enums.SourceEnum;
import eu.profinit.githubgitlabservice.model.internal.GitUserWithProjects;

import java.util.Optional;

/**
 * Service for interacting with clients of Git-based services like GitHub or GitLab.
 */
public interface GitService {

    /**
     * Gets user with Git projects from the specified source.
     *
     * @param username username of the user
     * @param source   the source enum (e.g.: GITHUB, GITLAB)
     * @return an Optional containing the user with projects if found, or empty if not found
     */
    Optional<GitUserWithProjects> getUserForSource(String username, SourceEnum source);

}
