package mk.ukim.finki.wp.workspaces.dto;

import mk.ukim.finki.wp.workspaces.model.domain.Workspace;

import java.util.List;
import java.util.stream.Collectors;

public record CreateWorkspaceDto(
        String name,
        String description
) {
    public static CreateWorkspaceDto from(Workspace workspace) {
        return new CreateWorkspaceDto(
                workspace.getName(),
                workspace.getDescription()
        );
    }

    public static List<CreateWorkspaceDto> from(List<Workspace> workspaces) {
        return workspaces.stream().map(CreateWorkspaceDto::from).collect(Collectors.toList());
    }

    public Workspace toWorkspace(String name, String description) {
        return new Workspace(name, description);
    }
}
