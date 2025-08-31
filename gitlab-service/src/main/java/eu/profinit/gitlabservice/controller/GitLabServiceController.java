package eu.profinit.gitlabservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import eu.profinit.gitlabservice.controller.dto.UserWithProjects;
import eu.profinit.gitlabservice.service.GitLabService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GitLabServiceController {

  private final GitLabService gitLabService;

  @GetMapping(value = "/users/{user}")
  public ResponseEntity<UserWithProjects> getUser(@PathVariable(value = "user") final String user) {
    final UserWithProjects result = gitLabService.getUser(user);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
