package mk.ukim.finki.wp.workspaces.model.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.wp.workspaces.model.context.WorkspaceContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@Table(name = "workspace_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserWorkspace> userWorkspaces = new ArrayList<>();

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userWorkspaces = new ArrayList<>();
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Long workspaceId = WorkspaceContext.getCurrentWorkspaceId();
        if (workspaceId == null) {
            return List.of();
        }

        return userWorkspaces.stream()
                .filter(uw -> uw.getWorkspace().getId().equals(workspaceId))
                .map(uw -> new SimpleGrantedAuthority(uw.getRole().name()))
                .collect(Collectors.toList());
    }
}