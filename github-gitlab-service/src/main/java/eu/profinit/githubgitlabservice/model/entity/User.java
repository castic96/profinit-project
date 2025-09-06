package eu.profinit.githubgitlabservice.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data // TODO: mozna tam misto toho dat @Getter a @Setter, aby to nevytvarelo konstruktor s vsema parametry
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

    @Column
    private String name;

    @Column
    private String url;

    private LocalDateTime lastUpdate;

    @Column(name = "_created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
