package mk.ukim.finki.wp.workspaces.service.application;

import mk.ukim.finki.wp.workspaces.dto.DisplayWorkspaceDto;
import mk.ukim.finki.wp.workspaces.dto.EditWorkspaceDto;
import mk.ukim.finki.wp.workspaces.dto.WorkspaceWithRoleDto;

import java.util.List;
import java.util.Optional;

public interface WorkspaceApplicationService {
    List<DisplayWorkspaceDto> findAll();
    Optional<DisplayWorkspaceDto> openWorkspace(Long workspaceId, Long userId);
    Optional<EditWorkspaceDto> editWorkspace(Long workspaceId, Long userId, EditWorkspaceDto editWorkspaceDto);
    List<WorkspaceWithRoleDto> findAllPerUser(Long userId);

    boolean deleteWorkspace(Long workspaceId, Long userId);
}
