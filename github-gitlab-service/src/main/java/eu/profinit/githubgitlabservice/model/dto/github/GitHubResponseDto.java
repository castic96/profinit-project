package eu.profinit.githubgitlabservice.model.dto.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubResponseDto {
    private Long id;
    private String login;
    @JsonProperty("html_url")
    private String htmlUrl;
    private String name;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    @JsonProperty("public_repos")
    private Long publicRepos;
    @JsonProperty("repositories")
    private List<GitHubProjectResponseDto> projects;
}
