package eu.profinit.githubgitlabservice.service.impl;

import eu.profinit.githubgitlabservice.mapper.ProjectMapper;
import eu.profinit.githubgitlabservice.mapper.UserMapper;
import eu.profinit.githubgitlabservice.model.entity.Project;
import eu.profinit.githubgitlabservice.model.entity.Source;
import eu.profinit.githubgitlabservice.model.entity.User;
import eu.profinit.githubgitlabservice.model.entity.Username;
import eu.profinit.githubgitlabservice.model.enums.SourceEnum;
import eu.profinit.githubgitlabservice.model.internal.GitUserWithProjects;
import eu.profinit.githubgitlabservice.repository.SourceRepository;
import eu.profinit.githubgitlabservice.repository.UserRepository;
import eu.profinit.githubgitlabservice.repository.UsernameRepository;
import eu.profinit.githubgitlabservice.service.DatabaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultDatabaseService implements DatabaseService {

    private final SourceRepository sourceRepository;
    private final UsernameRepository usernameRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;

    @Override
    public Source getSource(SourceEnum source) {
        Optional<Source> sourceEntity = sourceRepository.findByName(source);

        if (sourceEntity.isEmpty()) {
            log.warn("Source entity not found in the database for source: {}", source);
            return null;
        }

        return sourceEntity.get();
    }

    @Override
    public Username getUsername(String username) {
        Optional<Username> usernameEntity = usernameRepository.findByName(username);

        if (usernameEntity.isEmpty()) {
            log.info("Username entity not found in the database for username: {}", username);
            return createNewUsernameEntity(username);
        }

        return usernameEntity.get();
    }

    @Override
    public Optional<User> getUser(Username username, Source source) {
        return userRepository.findByUsernameAndSource(username, source);
    }

    @Override
    public User createNewUserEntity(Username username, Source source, Long gitId) {
        log.info("Creating new user entity for username: {} and source: {}", username.getName(), source.getName());

        User newUserEntity = new User();
        newUserEntity.setUsername(username);
        newUserEntity.setSource(source);
        newUserEntity.setGitId(gitId);

        return userRepository.save(newUserEntity);
    }

    @Override
    public void saveUserWithProjects(GitUserWithProjects gitUserWithProjects, User userEntity) {
        log.info("Saving user entity to database for username: {} and source: {}",
                userEntity.getUsername().getName(), userEntity.getSource().getName());

        userMapper.internalToEntity(gitUserWithProjects, userEntity);

        saveProjects(gitUserWithProjects, userEntity);

        userRepository.save(userEntity);
    }

    private void saveProjects(GitUserWithProjects gitUserWithProjects, User userEntity) {
        Map<Long, Project> existingProjects = userEntity.getProjects().stream()
                .collect(Collectors.toMap(Project::getGitId, p -> p));

        List<Project> updatedProjects = new ArrayList<>();

        gitUserWithProjects.getProjects().forEach(gitProject -> {
            Project project = existingProjects.getOrDefault(gitProject.getGitId(), new Project());

            if (project.getId() == null) {
                project.setUser(userEntity);
            }

            projectMapper.internalToEntity(gitProject, project);
            updatedProjects.add(project);

        });

        userEntity.getProjects().clear();
        userEntity.getProjects().addAll(updatedProjects);

        userRepository.save(userEntity);
    }

    private Username createNewUsernameEntity(String username) {
        log.info("Creating new username entity for username: {}", username);

        Username newUsernameEntity = new Username();
        newUsernameEntity.setName(username);

        return usernameRepository.save(newUsernameEntity);
    }

}
