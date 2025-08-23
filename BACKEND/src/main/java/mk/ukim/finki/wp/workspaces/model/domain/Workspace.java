package mk.ukim.finki.wp.workspaces.model.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Workspace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL)
    private List<UserWorkspace> userWorkspaces;

    public Workspace(String name, String description) {
        this.name = name;
        this.description = description;
        this.userWorkspaces = new ArrayList<>();
    }
}