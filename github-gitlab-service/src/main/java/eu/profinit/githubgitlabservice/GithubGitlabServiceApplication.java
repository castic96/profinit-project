package eu.profinit.githubgitlabservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class GithubGitlabServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GithubGitlabServiceApplication.class, args);
	}

}
