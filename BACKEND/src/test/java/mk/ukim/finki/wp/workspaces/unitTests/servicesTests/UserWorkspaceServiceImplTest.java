package mk.ukim.finki.wp.workspaces.unitTests.servicesTests;

import mk.ukim.finki.wp.workspaces.model.domain.User;
import mk.ukim.finki.wp.workspaces.model.domain.UserWorkspace;
import mk.ukim.finki.wp.workspaces.model.domain.Workspace;
import mk.ukim.finki.wp.workspaces.model.enumerations.Role;
import mk.ukim.finki.wp.workspaces.repository.UserWorkspaceRepository;
import mk.ukim.finki.wp.workspaces.service.domain.impl.UserWorkspaceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserWorkspaceServiceImplTest {

    @Mock
    private UserWorkspaceRepository userWorkspaceRepository;

    @InjectMocks
    private UserWorkspaceServiceImpl userWorkspaceService;

    private UserWorkspace testUserWorkspace1;
    private UserWorkspace testUserWorkspace2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setting up test data for UserWorkspaces
        User user1 = new User();
        user1.setId(1L);
        Workspace workspace1 = new Workspace();
        workspace1.setId(1L);

        testUserWorkspace1 = new UserWorkspace();
        testUserWorkspace1.setId(1L);
        testUserWorkspace1.setRole(Role.ROLE_ADMIN);
        testUserWorkspace1.setUser(user1);  // Ensuring the User is set
        testUserWorkspace1.setWorkspace(workspace1);  // Ensuring the Workspace is set

        User user2 = new User();
        user2.setId(2L);
        Workspace workspace2 = new Workspace();
        workspace2.setId(2L);

        testUserWorkspace2 = new UserWorkspace();
        testUserWorkspace2.setId(2L);
        testUserWorkspace2.setRole(Role.ROLE_VISITOR);
        testUserWorkspace2.setUser(user2);  // Ensuring the User is set
        testUserWorkspace2.setWorkspace(workspace2);  // Ensuring the Workspace is set
    }

    // Test for workspacesWithRolesForUser()
    @Test
    void testWorkspacesWithRolesForUser_Success() {
        // Arrange
        Long userId = 1L;

        // Set up the user and workspace objects properly
        User testUser = new User();
        testUser.setId(1L); // Set the user ID correctly

        Workspace testWorkspace1 = new Workspace();
        testWorkspace1.setId(1L);  // Set workspace ID correctly

        Workspace testWorkspace2 = new Workspace();
        testWorkspace2.setId(2L);  // Set workspace ID correctly

        // Create UserWorkspace objects and associate with the User and Workspace
        UserWorkspace testUserWorkspace1 = new UserWorkspace();
        testUserWorkspace1.setId(1L);
        testUserWorkspace1.setRole(Role.ROLE_ADMIN);
        testUserWorkspace1.setUser(testUser);
        testUserWorkspace1.setWorkspace(testWorkspace1);

        UserWorkspace testUserWorkspace2 = new UserWorkspace();
        testUserWorkspace2.setId(2L);
        testUserWorkspace2.setRole(Role.ROLE_VISITOR);
        testUserWorkspace2.setUser(testUser);
        testUserWorkspace2.setWorkspace(testWorkspace2);

        List<UserWorkspace> userWorkspaces = List.of(testUserWorkspace1, testUserWorkspace2);

        when(userWorkspaceRepository.findAll()).thenReturn(userWorkspaces);

        // Act
        Map<Long, Role> result = userWorkspaceService.workspacesWithRolesForUser(userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());  // Ensure that two entries are returned
        assertTrue(result.containsKey(1L));
        assertTrue(result.containsKey(2L));
        assertEquals(Role.ROLE_ADMIN, result.get(1L));
        assertEquals(Role.ROLE_VISITOR, result.get(2L));
        verify(userWorkspaceRepository, times(1)).findAll();
    }



    @Test
    void testWorkspacesWithRolesForUser_NoMatchingUser() {
        // Arrange
        Long userId = 3L;
        List<UserWorkspace> userWorkspaces = List.of(testUserWorkspace1, testUserWorkspace2);

        when(userWorkspaceRepository.findAll()).thenReturn(userWorkspaces);

        // Act
        Map<Long, Role> result = userWorkspaceService.workspacesWithRolesForUser(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userWorkspaceRepository, times(1)).findAll();
    }

    @Test
    void testWorkspacesWithRolesForUser_EmptyList() {
        // Arrange
        Long userId = 1L;
        List<UserWorkspace> userWorkspaces = List.of();  // Empty list of UserWorkspaces

        when(userWorkspaceRepository.findAll()).thenReturn(userWorkspaces);

        // Act
        Map<Long, Role> result = userWorkspaceService.workspacesWithRolesForUser(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());  // No user workspaces available for the given user ID
        verify(userWorkspaceRepository, times(1)).findAll();
    }
}
