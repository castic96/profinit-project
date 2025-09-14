package eu.profinit.githubgitlabservice.service;

import eu.profinit.githubgitlabservice.dto.UserResponse;

public interface UserService {
    UserResponse getUser(String username);
}
