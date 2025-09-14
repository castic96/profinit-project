package eu.profinit.githubgitlabservice.exception;

import lombok.Getter;

@Getter
public class GitClientException extends RuntimeException {
    private final String username;
    private final Integer statusCode;

    public GitClientException(String message, String username, Integer statusCode) {
        super(message);
        this.username = username;
        this.statusCode = statusCode;
    }
}
