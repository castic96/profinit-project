package eu.profinit.githubgitlabservice.repository;

import eu.profinit.githubgitlabservice.model.entity.User;
import eu.profinit.githubgitlabservice.model.entity.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUsername(Username username);
}
