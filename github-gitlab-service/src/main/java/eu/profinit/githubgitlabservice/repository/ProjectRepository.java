package eu.profinit.githubgitlabservice.repository;

import eu.profinit.githubgitlabservice.model.entity.Project;
import eu.profinit.githubgitlabservice.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByUser(User user);

}
