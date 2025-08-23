package mk.ukim.finki.wp.workspaces.service.application;

import mk.ukim.finki.wp.workspaces.dto.CreateUserDto;
import mk.ukim.finki.wp.workspaces.dto.DisplayUserDto;
import mk.ukim.finki.wp.workspaces.dto.LoginResponseDto;
import mk.ukim.finki.wp.workspaces.dto.LoginUserDto;

import java.util.Optional;

public interface UserApplicationService {
    Optional<DisplayUserDto> register(CreateUserDto createUserDto);

    Optional<LoginResponseDto> login(LoginUserDto loginUserDto);

    Optional<DisplayUserDto> findByUsername(String username);
}
