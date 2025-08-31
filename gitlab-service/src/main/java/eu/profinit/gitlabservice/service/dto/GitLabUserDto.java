package eu.profinit.gitlabservice.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitLabUserDto {
  private Long id;
  private String username;
  private String name;
  private String state;
  private Boolean locked;

  @JsonProperty("avatar_url")
  private String avatarUrl;

  @JsonProperty("web_url")
  private String webUrl;
}
