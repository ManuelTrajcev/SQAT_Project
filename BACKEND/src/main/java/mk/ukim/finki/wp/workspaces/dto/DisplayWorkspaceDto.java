package mk.ukim.finki.wp.workspaces.dto;

import mk.ukim.finki.wp.workspaces.model.domain.User;
import mk.ukim.finki.wp.workspaces.model.domain.Workspace;

public record DisplayWorkspaceDto(
        Long id,
        String name,
        String description
) {
    public static DisplayWorkspaceDto from(Workspace workspace) {
        return new DisplayWorkspaceDto(
                workspace.getId(),
                workspace.getName(),
                workspace.getDescription()
        );
    }

    public Workspace toWorkspace() {
        return new Workspace(name, description);
    }
}
