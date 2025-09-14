package eu.profinit.gitlabservice.service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import eu.profinit.gitlabservice.controller.dto.UserWithProjects;
import eu.profinit.gitlabservice.database.DatabaseService;
import eu.profinit.gitlabservice.database.model.GitLabProject;
import eu.profinit.gitlabservice.database.model.GitLabUser;
import eu.profinit.gitlabservice.service.dto.GitLabProjectDto;
import eu.profinit.gitlabservice.service.dto.GitLabUserDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GitLabService {

  private final DatabaseService databaseService;
  private final RestTemplate restTemplate;
  private final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
  private final int CACHE_TIME_30_SECONDS = 30;
  private final String GITLAB_USER_URL = "https://gitlab.com/api/v4/users?username=%s";
  private final String GITLAB_PROJECTS_URL = "https://gitlab.com/api/v4/users/%s/projects";

  public UserWithProjects getUser(final String username) {
    final Optional<GitLabUser> user = databaseService.getUser(username);
    if (user.isPresent()) {
      if (Instant.now().getEpochSecond() - user.get().getUpdated().getEpochSecond() > CACHE_TIME_30_SECONDS) {
        return refreshDatabaseCache(username);
      } else {
        return new UserWithProjects(
                user.get(), databaseService.getProjects(username), SDF.format(new Date()));
      }
    } else {
      return refreshDatabaseCache(username);
    }
  }

  @Transactional
  public UserWithProjects refreshDatabaseCache(final String username) {
    final GitLabUserDto userDto = getGitLabUser(username);
    final List<GitLabProjectDto> projectsDto = getGitLabUserProjects(username);

    final GitLabUser user = databaseService.saveUser(userDto);
    final List<GitLabProject> projects =
            projectsDto.stream().map(p -> databaseService.saveProject(userDto, p)).toList();

    return new UserWithProjects(user, projects, SDF.format(new Date()));
  }

  private List<GitLabProjectDto> getGitLabUserProjects(final String user) {
    final String url = String.format(GITLAB_PROJECTS_URL, user);
    final ResponseEntity<List<GitLabProjectDto>> response =
            restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
            });

    return response.getBody();
  }

  private GitLabUserDto getGitLabUser(final String user) {
    final String url = String.format(GITLAB_USER_URL, user);
    final ResponseEntity<List<GitLabUserDto>> response =
            restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
            });
    if (response.getBody() != null && !response.getBody().isEmpty()) {
      return response.getBody().get(0);
    }
    return null;
  }
}
