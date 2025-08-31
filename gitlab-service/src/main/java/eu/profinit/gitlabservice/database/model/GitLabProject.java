package eu.profinit.gitlabservice.database.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity(name = "gitlab_project")
@Data
public class GitLabProject {
  @Id
  private Long id;
  private String description;
  private String name;
  private String webUrl;

  @ManyToOne
  @JoinColumn(name = "gitLabUserId")
  private GitLabUser gitLabUser;
}
