package eu.profinit.githubgitlabservice.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usernames")
@Data
public class Username {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "_created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

}
