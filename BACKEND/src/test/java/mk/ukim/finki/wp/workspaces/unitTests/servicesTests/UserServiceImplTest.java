package mk.ukim.finki.wp.workspaces.unitTests.servicesTests;
import mk.ukim.finki.wp.workspaces.model.domain.User;
import mk.ukim.finki.wp.workspaces.model.exceptions.InvalidArgumentsException;
import mk.ukim.finki.wp.workspaces.model.exceptions.InvalidUserCredentialsException;
import mk.ukim.finki.wp.workspaces.model.exceptions.PasswordsDoNotMatchException;
import mk.ukim.finki.wp.workspaces.model.exceptions.UserNotFoundException;
import mk.ukim.finki.wp.workspaces.model.exceptions.UsernameAlreadyExistsException;
import mk.ukim.finki.wp.workspaces.repository.UserRepository;
import mk.ukim.finki.wp.workspaces.service.domain.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setting up a test user
        testUser = new User("testUser", "test@example.com", "testPassword");
        testUser.setId(1L);
    }

    // Test for loadUserByUsername
    @Test
    void testLoadUserByUsername_Success() {
        // Arrange
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        // Act
        UserDetails userDetails = userService.loadUserByUsername("testUser");

        // Assert
        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        // Arrange
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("testUser");
        });
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    // Test for findByUsername
    @Test
    void testFindByUsername_Success() {
        // Arrange
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        // Act
        User user = userService.findByUsername("testUser");

        // Assert
        assertNotNull(user);
        assertEquals("testUser", user.getUsername());
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    void testFindByUsername_NotFound() {
        // Arrange
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.findByUsername("testUser");
        });
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    // Test for register
    @Test
    void testRegister_Success() {
        // Arrange
        String username = "newUser";
        String email = "newUser@example.com";
        String password = "password123";
        String repeatPassword = "password123";

        // Mock the behavior of userRepository
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        // Mock the save method to return the user object
        when(userRepository.save(any(User.class))).thenReturn(new User(username, email, "encodedPassword"));

        // Act
        User newUser = userService.register(username, email, password, repeatPassword);

        // Assert
        assertNotNull(newUser);
        assertEquals(username, newUser.getUsername());
        assertEquals(email, newUser.getEmail());
        assertEquals("encodedPassword", newUser.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }


    @Test
    void testRegister_PasswordsDoNotMatch() {
        // Arrange
        String username = "newUser";
        String email = "newUser@example.com";
        String password = "password123";
        String repeatPassword = "password456";

        // Act & Assert
        assertThrows(PasswordsDoNotMatchException.class, () -> {
            userService.register(username, email, password, repeatPassword);
        });
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void testRegister_UsernameAlreadyExists() {
        // Arrange
        String username = "existingUser";
        String email = "existingUser@example.com";
        String password = "password123";
        String repeatPassword = "password123";

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));

        // Act & Assert
        assertThrows(UsernameAlreadyExistsException.class, () -> {
            userService.register(username, email, password, repeatPassword);
        });
        verify(userRepository, times(0)).save(any());
    }

    // Test for login
    @Test
    void testLogin_Success() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(password, testUser.getPassword())).thenReturn(true);

        // Act
        User loggedInUser = userService.login(username, password);

        // Assert
        assertNotNull(loggedInUser);
        assertEquals(username, loggedInUser.getUsername());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testLogin_InvalidUsername() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            userService.login(username, password);
        });
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testLogin_InvalidPassword() {
        // Arrange
        String username = "testUser";
        String password = "wrongPassword";

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(password, testUser.getPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidUserCredentialsException.class, () -> {
            userService.login(username, password);
        });
        verify(userRepository, times(1)).findByUsername(username);
    }
}
