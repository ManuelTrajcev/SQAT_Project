package mk.ukim.finki.wp.workspaces.dto;

import mk.ukim.finki.wp.workspaces.model.domain.Workspace;

public record EditWorkspaceDto(
        String name,
        String description,
        Boolean hasPermissionToEdit
) {
    public static EditWorkspaceDto from(Workspace workspace) {
        return new EditWorkspaceDto(
                workspace.getName(),
                workspace.getDescription(),
                true
        );
    }

    public Workspace toWorkspace() {
        return new Workspace(name, description);
    }
}
