package eu.profinit.gitlabservice.database.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.profinit.gitlabservice.database.model.GitLabProject;

@Repository
public interface GitLabProjectRepository extends JpaRepository<GitLabProject, Long> {
    List<GitLabProject> findByGitLabUserUsername(String username);
}
