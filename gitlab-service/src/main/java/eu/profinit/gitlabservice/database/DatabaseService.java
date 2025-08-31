package eu.profinit.gitlabservice.database;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import eu.profinit.gitlabservice.database.model.GitLabProject;
import eu.profinit.gitlabservice.database.model.GitLabUser;
import eu.profinit.gitlabservice.database.repository.GitLabProjectRepository;
import eu.profinit.gitlabservice.database.repository.GitLabUserRepository;
import eu.profinit.gitlabservice.service.dto.GitLabProjectDto;
import eu.profinit.gitlabservice.service.dto.GitLabUserDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DatabaseService {

  private final GitLabUserRepository gitLabUserRepository;
  private final GitLabProjectRepository gitLabProjectRepository;

  public Optional<GitLabUser> getUser(final String username) {
    return gitLabUserRepository.findByUsername(username);
  }

  public GitLabUser saveUser(final GitLabUserDto userDto) {
    final GitLabUser user = new GitLabUser();
    user.setId(userDto.getId());
    user.setName(userDto.getName());
    user.setState(userDto.getState());
    user.setUsername(userDto.getUsername());
    user.setAvatarUrl(userDto.getAvatarUrl());
    user.setWebUrl(userDto.getWebUrl());
    user.setUpdated(Instant.now());

    return gitLabUserRepository.save(user);
  }

  public GitLabProject saveProject(final GitLabUserDto userDto, final GitLabProjectDto projectDto) {
    final Optional<GitLabUser> user = gitLabUserRepository.findById(userDto.getId());
    if (user.isEmpty()) {
      saveUser(userDto);
    }
    final GitLabProject project = new GitLabProject();
    project.setId(projectDto.getId());
    project.setName(projectDto.getName());
    project.setDescription(projectDto.getDescription());
    project.setWebUrl(projectDto.getWebUrl());

    return gitLabProjectRepository.save(project);
  }

  public List<GitLabProject> getProjects(final String username) {
    return gitLabProjectRepository.findByGitLabUserUsername(username);
  }
}
