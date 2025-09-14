package eu.profinit.githubgitlabservice.model.internal;

import lombok.Data;

@Data
public class GitProject {
    private Long gitId;
    private String projectName;
    private String description;
    private String url;
}
