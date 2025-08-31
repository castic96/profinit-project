package eu.profinit.gitlabservice.controller.dto;

import java.util.List;

import eu.profinit.gitlabservice.database.model.GitLabProject;
import eu.profinit.gitlabservice.database.model.GitLabUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithProjects {

  private GitLabUser user;

  private List<GitLabProject> projects;

  private String updated;
}
