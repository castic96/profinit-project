package eu.profinit.githubgitlabservice.service;

import eu.profinit.githubgitlabservice.dto.UserResponse;

/**
 * Service for user-related operations.
 */
public interface UserService {

    /**
     * Retrieves user and Git projects based on the provided username.
     *
     * @param username the username of the user
     * @return UserResponse containing user and projects information
     */
    UserResponse getUser(String username);
}
