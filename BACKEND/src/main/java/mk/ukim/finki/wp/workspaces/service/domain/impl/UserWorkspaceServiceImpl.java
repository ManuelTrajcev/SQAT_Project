package mk.ukim.finki.wp.workspaces.service.domain.impl;

import mk.ukim.finki.wp.workspaces.model.domain.UserWorkspace;
import mk.ukim.finki.wp.workspaces.model.enumerations.Role;
import mk.ukim.finki.wp.workspaces.repository.UserWorkspaceRepository;
import mk.ukim.finki.wp.workspaces.service.domain.UserWorkspaceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserWorkspaceServiceImpl implements UserWorkspaceService {
    private final UserWorkspaceRepository userWorkspaceRepository;

    public UserWorkspaceServiceImpl(UserWorkspaceRepository userWorkspaceRepository) {
        this.userWorkspaceRepository = userWorkspaceRepository;
    }

    @Override
    public Map<Long, Role> workspacesWithRolesForUser(Long id) {
        List<UserWorkspace> list =  userWorkspaceRepository.findAll();
        Map<Long, Role> claims  = list.stream()
                .filter(uw -> uw.getUser().getId().equals(id))
                .collect(Collectors.toMap(
                        uv -> uv.getWorkspace().getId(),
                        UserWorkspace::getRole
                ));
        return claims;
    }


}
