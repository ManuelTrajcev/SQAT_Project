package mk.ukim.finki.wp.workspaces.service.application.impl;

import mk.ukim.finki.wp.workspaces.dto.DisplayWorkspaceDto;
import mk.ukim.finki.wp.workspaces.dto.EditWorkspaceDto;
import mk.ukim.finki.wp.workspaces.dto.WorkspaceWithRoleDto;
import mk.ukim.finki.wp.workspaces.model.domain.Workspace;
import mk.ukim.finki.wp.workspaces.service.application.WorkspaceApplicationService;
import mk.ukim.finki.wp.workspaces.service.domain.WorkspaceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.stream;

@Service
public class WorkspaceApplicationServiceImpl implements WorkspaceApplicationService {
    private final WorkspaceService workspaceService;

    public WorkspaceApplicationServiceImpl(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @Override
    public List<DisplayWorkspaceDto> findAll() {
        return workspaceService.findAll()
                .stream()
                .map(DisplayWorkspaceDto::from)
                .toList();
    }

    @Override
    public Optional<DisplayWorkspaceDto> openWorkspace(Long workspaceId, Long userId) {
        return Optional.ofNullable(workspaceService.openWorkspace(workspaceId, userId)
                .map(DisplayWorkspaceDto::from)
                .orElseThrow(() -> new RuntimeException("Workspace not found")));
    }

    @Override
    public Optional<EditWorkspaceDto> editWorkspace(Long workspaceId, Long userId, EditWorkspaceDto editWorkspaceDto) {
        return Optional.ofNullable(workspaceService.editWorkspace(workspaceId, userId, editWorkspaceDto.toWorkspace())
                .map(EditWorkspaceDto::from)
                .orElseThrow(() -> new RuntimeException("Workspace not found")));
    }

    @Override
    public List<WorkspaceWithRoleDto> findAllPerUser(Long userId) {
        return workspaceService
                .findAllPerUser(userId)
                .stream()
                .map(WorkspaceWithRoleDto::from)
                .toList();
    }

    @Override
    public boolean deleteWorkspace(Long workspaceId, Long userId) {
        return workspaceService.deleteWorkspace(workspaceId, userId);
    }

}
