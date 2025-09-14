package eu.profinit.githubgitlabservice.service;

import eu.profinit.githubgitlabservice.model.entity.Source;
import eu.profinit.githubgitlabservice.model.entity.User;
import eu.profinit.githubgitlabservice.model.entity.Username;
import eu.profinit.githubgitlabservice.model.enums.SourceEnum;
import eu.profinit.githubgitlabservice.model.internal.GitUserWithProjects;

import java.util.Optional;

/**
 * Service for database operations related to users, projects and sources.
 */
public interface DatabaseService {

    /**
     * Gets the Source entity corresponding to the given SourceEnum.
     *
     * @param source the source enum (e.g.: GITHUB, GITLAB)
     * @return the Source entity
     */
    Source getSource(SourceEnum source);

    /**
     * Gets the Username entity corresponding to the given username string.
     *
     * @param username the username string of the user
     * @return the Username entity
     */
    Username getUsername(String username);

    /**
     * Gets the User entity for the given Username and Source entities.
     *
     * @param username the Username entity of the user
     * @param source   the Source entity (e.g.: GITHUB, GITLAB)
     * @return an Optional containing the User entity if found, or empty if not found
     */
    Optional<User> getUser(Username username, Source source);

    /**
     * Creates a new User entity with the given Username, Source and gitId.
     *
     * @param username the Username entity of the user
     * @param source   the Source entity (e.g.: GITHUB, GITLAB)
     * @param gitId    the ID of the user in the Git service
     * @return the created User entity
     */
    User createNewUserEntity(Username username, Source source, Long gitId);

    /**
     * Saves the given GitUserWithProjects data into the database, associating it with the provided User entity.
     *
     * @param gitUserWithProjects the GitUserWithProjects data to be saved
     * @param userEntity          the User entity to associate with the projects
     */
    void saveUserWithProjects(GitUserWithProjects gitUserWithProjects, User userEntity);
}
