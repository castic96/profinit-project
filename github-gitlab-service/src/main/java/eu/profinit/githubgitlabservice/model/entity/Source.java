package eu.profinit.githubgitlabservice.model.entity;

import eu.profinit.githubgitlabservice.model.enums.SourceEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity representing a Source (e.g., GitHub, GitLab).
 */
@Entity
@Table(name = "sources")
@Data
public class Source {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private SourceEnum name;

    @Column(name = "_created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}
