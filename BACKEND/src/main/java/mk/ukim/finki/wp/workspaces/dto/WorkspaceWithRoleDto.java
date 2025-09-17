package mk.ukim.finki.wp.workspaces.dto;

import mk.ukim.finki.wp.workspaces.model.domain.UserWorkspace;
import mk.ukim.finki.wp.workspaces.model.domain.Workspace;
import mk.ukim.finki.wp.workspaces.model.enumerations.Role;

import java.util.List;
import java.util.stream.Collectors;

public record WorkspaceWithRoleDto(
        Long id,
        String name,
        String description,
        Role role
) {
    public static WorkspaceWithRoleDto from(UserWorkspace userWorkspace) {
        return new WorkspaceWithRoleDto(
                userWorkspace.getWorkspace().getId(),
                userWorkspace.getWorkspace().getName(),
                userWorkspace.getWorkspace().getDescription(),
                userWorkspace.getRole()
        );
    }

    public static List<WorkspaceWithRoleDto> from(List<UserWorkspace> workspaces) {
        return workspaces.stream().map(WorkspaceWithRoleDto::from).collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role.name();
    }
}