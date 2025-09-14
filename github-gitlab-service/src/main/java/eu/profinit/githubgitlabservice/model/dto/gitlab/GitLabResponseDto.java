package eu.profinit.githubgitlabservice.model.dto.gitlab;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitLabResponseDto {
    private GitLabUserResponseDto user;

    private List<GitLabProjectResponseDto> projects;

    private String updated;

}
