package eu.profinit.githubgitlabservice.service.impl;

import eu.profinit.githubgitlabservice.client.GitApiClient;
import eu.profinit.githubgitlabservice.mapper.UserMapper;
import eu.profinit.githubgitlabservice.model.entity.Source;
import eu.profinit.githubgitlabservice.model.entity.User;
import eu.profinit.githubgitlabservice.model.entity.Username;
import eu.profinit.githubgitlabservice.model.enums.SourceEnum;
import eu.profinit.githubgitlabservice.model.internal.GitUserWithProjects;
import eu.profinit.githubgitlabservice.service.DatabaseService;
import eu.profinit.githubgitlabservice.service.GitService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultGitService implements GitService {

    private final Map<SourceEnum, GitApiClient> gitApiClients;
    private final DatabaseService databaseService;
    private final UserMapper userMapper;

    @Value("${cache.ttl-seconds}")
    private int cacheTtlSeconds;

    @Override
    @Transactional
    public Optional<GitUserWithProjects> getUserForSource(String username, SourceEnum source) {
        log.info("Getting user with username '{}' from source '{}'", username, source);

        // Resolve the source entity
        if (!isSourceValid(source)) {
            return Optional.empty();
        }

        Source sourceEntity = databaseService.getSource(source);

        if (sourceEntity == null) {
            return Optional.empty();
        }

        // Resolve the username entity
        Username usernameEntity = databaseService.getUsername(username);

        return getUserWithProjects(sourceEntity, usernameEntity);
    }

    private Optional<GitUserWithProjects> getUserWithProjects(Source sourceEntity, Username usernameEntity) {
        Optional<User> userEntityOpt = databaseService.getUser(usernameEntity, sourceEntity);

        // User not found in DB -> fetch from API and save
        if (userEntityOpt.isEmpty()) {
            return fetchAndSaveUser(usernameEntity, sourceEntity, userEntityOpt);
        }

        User userEntity = userEntityOpt.get();

        // User found in DB but cache expired -> fetch from API and update
        if (isCacheExpired(userEntity)) {
            Optional<GitUserWithProjects> gitUserWithProjects = fetchAndSaveUser(usernameEntity, sourceEntity, userEntityOpt);

            if (gitUserWithProjects.isPresent()) {
                return gitUserWithProjects;
            }

        }

        // User exists and cache is valid (or not fetched from API) -> return mapped entity
        return Optional.of(userMapper.entityToInternal(userEntity));
    }

    private Optional<GitUserWithProjects> fetchAndSaveUser(Username username, Source source, Optional<User> userEntityOpt) {
        GitApiClient gitApiClient = gitApiClients.get(source.getName());
        Optional<GitUserWithProjects> gitUserWithProjects = gitApiClient.getUserWithProjects(username.getName());

        gitUserWithProjects.ifPresent(gitUser -> {
            User userEntity = userEntityOpt.orElseGet(() ->
                    databaseService.createNewUserEntity(username, source, gitUser.getGitId()));
            databaseService.saveUserWithProjects(gitUser, userEntity);
        });

        return gitUserWithProjects;
    }

    private boolean isCacheExpired(User userEntity) {
        LocalDateTime lastUpdate = userEntity.getLastUpdate();
        return lastUpdate == null || lastUpdate.isBefore(LocalDateTime.now().minusSeconds(cacheTtlSeconds));
    }

    private boolean isSourceValid(SourceEnum source) {
        if (!gitApiClients.containsKey(source)) {
            log.error("No GitApiClient found for source: {}", source);
            return false;
        }
        return true;
    }

}
