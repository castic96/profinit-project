package eu.profinit.githubgitlabservice.model.dto.gitlab;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitLabUserResponseDto {
    private Long id;
    private String username;
    private String name;
    private String state;
    private Boolean locked;
    private String avatarUrl;
    private String webUrl;
}
