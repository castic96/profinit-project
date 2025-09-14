package eu.profinit.githubgitlabservice.model.dto.gitlab;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitLabProjectResponseDto {
    private Long id;
    private String description;
    private String name;
    private String webUrl;
}
