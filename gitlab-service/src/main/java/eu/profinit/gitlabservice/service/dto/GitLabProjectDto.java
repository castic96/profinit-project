package eu.profinit.gitlabservice.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitLabProjectDto {

  private Long id;
  private String description;
  private String name;

  @JsonProperty("web_url")
  private String webUrl;
}
