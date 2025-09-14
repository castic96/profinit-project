package eu.profinit.githubgitlabservice.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "username_id", nullable = false)
    private Username username;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "source_id", nullable = false)
    private Source source;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects = new ArrayList<>();

    @Column(nullable = false)
    private Long gitId;

    @Column
    private String name;

    @Column
    private String url;

    private LocalDateTime lastUpdate;

    @Column(name = "_created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
