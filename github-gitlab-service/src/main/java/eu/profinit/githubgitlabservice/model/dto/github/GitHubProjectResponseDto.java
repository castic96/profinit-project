package eu.profinit.githubgitlabservice.model.dto.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubProjectResponseDto {
    private String id;
    private String name;
    private String description;
    @JsonProperty("html_url")
    private String htmlUrl;
    private String language;
}
