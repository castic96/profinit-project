package eu.profinit.githubgitlabservice.service;

import eu.profinit.githubgitlabservice.model.enums.SourceEnum;
import eu.profinit.githubgitlabservice.model.internal.GitUserWithProjects;

import java.util.Optional;

public interface GitService {

    Optional<GitUserWithProjects> getUserForSource(String username, SourceEnum source);

}
