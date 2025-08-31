package eu.profinit.gitlabservice.database.model;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name = "gitlab_user")
@Data
public class GitLabUser {
  @Id
  private Long id;
  private String username;
  private String name;
  private String state;
  private Boolean locked;
  private String avatarUrl;
  private String webUrl;
  private Instant updated;
}
