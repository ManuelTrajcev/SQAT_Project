package mk.ukim.finki.wp.workspaces.service.domain;

import mk.ukim.finki.wp.workspaces.model.enumerations.Role;

import java.util.Map;

public interface UserWorkspaceService {
    Map<Long, Role> workspacesWithRolesForUser(Long userId);

}
