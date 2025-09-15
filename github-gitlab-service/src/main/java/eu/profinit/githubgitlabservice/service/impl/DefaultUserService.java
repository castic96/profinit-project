package eu.profinit.githubgitlabservice.service.impl;

import eu.profinit.githubgitlabservice.dto.UserResponse;
import eu.profinit.githubgitlabservice.mapper.UserMapper;
import eu.profinit.githubgitlabservice.model.enums.SourceEnum;
import eu.profinit.githubgitlabservice.model.internal.GitUserWithProjects;
import eu.profinit.githubgitlabservice.service.GitService;
import eu.profinit.githubgitlabservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation of the UserService interface.
 * This service retrieves user information from various Git-based services (e.g., GitHub, GitLab)
 * and maps it to a unified response format.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final GitService gitService;
    private final UserMapper userMapper;

    @Override
    public UserResponse getUser(String username) {
        log.info("Getting user with username '{}'", username);

        Map<SourceEnum, GitUserWithProjects> users = processSources(username);

        return userMapper.internalMapToUserResponse(users);
    }

    private Map<SourceEnum, GitUserWithProjects> processSources(String username) {
        Map<SourceEnum, GitUserWithProjects> users = new EnumMap<>(SourceEnum.class);

        // Iterate over all sources and attempt to retrieve the user from each
        Arrays.stream(SourceEnum.values()).forEach(source -> {
            Optional<GitUserWithProjects> user = processSource(username, source);

            user.ifPresent(gitUserWithProjects -> users.put(source, gitUserWithProjects));
        });

        return users;
    }

    private Optional<GitUserWithProjects> processSource(String username, SourceEnum source) {
        log.info("Trying to get user {} from source: {}", username, source.toString());

        // Retrieve user from the database service for the given source
        return gitService.getUserForSource(username, source);
    }

}
