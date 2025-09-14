package eu.profinit.githubgitlabservice.controller;

import eu.profinit.githubgitlabservice.dto.UserResponse;
import eu.profinit.githubgitlabservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${openapi.base-path}")
public class UserController implements UsersApi {

  private final UserService userService;

  @Override
  public ResponseEntity<UserResponse> getUserByUsername(String user) {
    return ResponseEntity.ok(userService.getUser(user));
  }
}
