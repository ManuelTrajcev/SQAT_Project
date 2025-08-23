package mk.ukim.finki.wp.workspaces.service.domain;

import mk.ukim.finki.wp.workspaces.model.domain.UserWorkspace;
import mk.ukim.finki.wp.workspaces.model.domain.Workspace;

import java.util.List;
import java.util.Optional;

public interface WorkspaceService {
    List<Workspace> findAll();

    Optional<Workspace> openWorkspace(Long workspaceId, Long userId);

    Optional<Workspace> editWorkspace(Long workspaceId, Long userId, Workspace editWorkspace);
    Optional<Workspace> findById(Long workspaceId);

    List<UserWorkspace> findAllPerUser(Long userId);
}
