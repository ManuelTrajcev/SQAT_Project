package mk.ukim.finki.wp.workspaces.service.application.impl;

import mk.ukim.finki.wp.workspaces.dto.CreateUserDto;
import mk.ukim.finki.wp.workspaces.dto.DisplayUserDto;
import mk.ukim.finki.wp.workspaces.dto.LoginResponseDto;
import mk.ukim.finki.wp.workspaces.dto.LoginUserDto;
import mk.ukim.finki.wp.workspaces.model.domain.User;
import mk.ukim.finki.wp.workspaces.model.enumerations.Role;
import mk.ukim.finki.wp.workspaces.repository.UserWorkspaceRepository;
import mk.ukim.finki.wp.workspaces.security.JwtHelper;
import mk.ukim.finki.wp.workspaces.service.application.UserApplicationService;
import mk.ukim.finki.wp.workspaces.service.domain.UserService;
import mk.ukim.finki.wp.workspaces.service.domain.UserWorkspaceService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserApplicationServiceImpl implements UserApplicationService {
    private final UserService userService;
    private final JwtHelper jwtHelper;
    private final UserWorkspaceRepository userWorkspaceRepository;
    private final UserWorkspaceService userWorkspaceService;

    public UserApplicationServiceImpl(UserService userService, JwtHelper jwtHelper, UserWorkspaceRepository userWorkspaceRepository, UserWorkspaceService userWorkspaceService) {
        this.userService = userService;
        this.jwtHelper = jwtHelper;
        this.userWorkspaceRepository = userWorkspaceRepository;
        this.userWorkspaceService = userWorkspaceService;
    }


    @Override
    public Optional<DisplayUserDto> register(CreateUserDto createUserDto) {
        User user = userService.register(
                createUserDto.username(),
                createUserDto.email(),
                createUserDto.password(),
                createUserDto.repeatPassword()
        );
        return Optional.of(DisplayUserDto.from(user));
    }

    @Override
    public Optional<LoginResponseDto> login(LoginUserDto loginUserDto) {
        User user = userService.login(
                loginUserDto.username(),
                loginUserDto.password()
        );

        Map<Long, Role> claims =userWorkspaceService.workspacesWithRolesForUser(user.getId());
        String token = jwtHelper.generateTokenWithWorkspacesAccess(user, claims);       //workspaceId, Role
        return Optional.of(new LoginResponseDto(token));
    }

    @Override
    public Optional<DisplayUserDto> findByUsername(String username) {
        return Optional.of(DisplayUserDto.from(userService.findByUsername(username)));
    }
}
