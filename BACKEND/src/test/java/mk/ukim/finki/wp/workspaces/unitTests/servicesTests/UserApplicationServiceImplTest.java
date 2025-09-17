package mk.ukim.finki.wp.workspaces.unitTests.servicesTests;

import mk.ukim.finki.wp.workspaces.dto.CreateUserDto;
import mk.ukim.finki.wp.workspaces.dto.DisplayUserDto;
import mk.ukim.finki.wp.workspaces.dto.LoginResponseDto;
import mk.ukim.finki.wp.workspaces.dto.LoginUserDto;
import mk.ukim.finki.wp.workspaces.model.domain.User;
import mk.ukim.finki.wp.workspaces.model.enumerations.Role;
import mk.ukim.finki.wp.workspaces.repository.UserWorkspaceRepository;
import mk.ukim.finki.wp.workspaces.security.JwtHelper;
import mk.ukim.finki.wp.workspaces.service.application.impl.UserApplicationServiceImpl;
import mk.ukim.finki.wp.workspaces.service.domain.UserService;
import mk.ukim.finki.wp.workspaces.service.domain.UserWorkspaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserApplicationServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtHelper jwtHelper;

    @Mock
    private UserWorkspaceRepository userWorkspaceRepository;

    @Mock
    private UserWorkspaceService userWorkspaceService;

    @InjectMocks
    private UserApplicationServiceImpl userApplicationService;

    private CreateUserDto createUserDto;
    private LoginUserDto loginUserDto;
    private User user;
    private DisplayUserDto displayUserDto;

    @BeforeEach
    public void setUp() {
        // Initialize DTOs and mock User object
        createUserDto = new CreateUserDto("testuser", "testemail@example.com", "password", "password");
        loginUserDto = new LoginUserDto("testuser", "password");
        user = new User("testuser", "testemail@example.com", "password");
        displayUserDto = DisplayUserDto.from(user);
    }

    @Test
    public void testRegister() {
        // Arrange: Mock the userService.register() method
        when(userService.register(createUserDto.username(), createUserDto.email(), createUserDto.password(), createUserDto.repeatPassword()))
                .thenReturn(user);

        // Act: Call the register method of the service
        Optional<DisplayUserDto> result = userApplicationService.register(createUserDto);

        // Assert: Verify the result is present and contains the correct user data
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
        verify(userService, times(1)).register(createUserDto.username(), createUserDto.email(), createUserDto.password(), createUserDto.repeatPassword());
    }

    @Test
    public void testLogin() {
        // Arrange: Mock the login process
        when(userService.login(loginUserDto.username(), loginUserDto.password())).thenReturn(user);

        Map<Long, Role> mockClaims = new HashMap<>();
        mockClaims.put(1L, Role.ROLE_ADMIN);

        when(userWorkspaceService.workspacesWithRolesForUser(user.getId())).thenReturn(mockClaims);
        when(jwtHelper.generateTokenWithWorkspacesAccess(user, mockClaims)).thenReturn("mockToken");

        // Act: Call the login method of the service
        Optional<LoginResponseDto> result = userApplicationService.login(loginUserDto);

        // Assert: Verify the result contains a token and the login was successful
        assertTrue(result.isPresent());
        assertEquals("mockToken", result.get().getToken());
        verify(userService, times(1)).login(loginUserDto.username(), loginUserDto.password());
        verify(userWorkspaceService, times(1)).workspacesWithRolesForUser(user.getId());
        verify(jwtHelper, times(1)).generateTokenWithWorkspacesAccess(user, mockClaims);
    }

    @Test
    public void testFindByUsername() {
        // Arrange: Mock the findByUsername() method
        when(userService.findByUsername("testuser")).thenReturn(user);

        // Act: Call the findByUsername method of the service
        Optional<DisplayUserDto> result = userApplicationService.findByUsername("testuser");

        // Assert: Verify the result is present and contains the correct user data
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
        verify(userService, times(1)).findByUsername("testuser");
    }
}
