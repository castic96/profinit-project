package eu.profinit.githubgitlabservice.model.internal;

import lombok.Data;

import java.util.List;

@Data
public class GitUserWithProjects {
    private Long gitId;
    private String username;
    private String url;
    private String name;
    private String lastUpdate;
    private List<GitProject> projects;
}
