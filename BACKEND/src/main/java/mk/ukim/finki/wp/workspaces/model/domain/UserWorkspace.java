package mk.ukim.finki.wp.workspaces.model.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.wp.workspaces.model.enumerations.Role;

@Entity
@Data
@NoArgsConstructor
public class UserWorkspace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Workspace workspace;

    @Enumerated(EnumType.STRING)
    private Role role;

    public UserWorkspace(User user, Workspace workspace, Role role) {
        this.user = user;
        this.workspace = workspace;
        this.role = role;
    }
}