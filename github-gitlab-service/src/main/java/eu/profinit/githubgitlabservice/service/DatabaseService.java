package eu.profinit.githubgitlabservice.service;

import eu.profinit.githubgitlabservice.model.entity.Source;
import eu.profinit.githubgitlabservice.model.entity.User;
import eu.profinit.githubgitlabservice.model.entity.Username;
import eu.profinit.githubgitlabservice.model.enums.SourceEnum;
import eu.profinit.githubgitlabservice.model.internal.GitUserWithProjects;

import java.util.Optional;

public interface DatabaseService {

    Source getSource(SourceEnum source);

    Username getUsername(String username);

    Optional<User> getUser(Username username, Source source);

    User createNewUserEntity(Username username, Source source, Long gitId);

    void saveUserWithProjects(GitUserWithProjects gitUserWithProjects, User userEntity);
}
