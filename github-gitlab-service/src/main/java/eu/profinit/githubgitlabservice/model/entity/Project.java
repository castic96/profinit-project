package eu.profinit.githubgitlabservice.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Entity representing a Project associated with a User.
 */
@Entity
@Table(name = "projects")
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Long gitId;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String url;

    @Column(name = "_created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
