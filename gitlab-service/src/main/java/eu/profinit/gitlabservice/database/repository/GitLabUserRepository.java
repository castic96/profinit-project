package eu.profinit.gitlabservice.database.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.profinit.gitlabservice.database.model.GitLabUser;

@Repository
public interface GitLabUserRepository extends JpaRepository<GitLabUser, Long> {

  Optional<GitLabUser> findByUsername(String username);
}
