package mk.ukim.finki.wp.workspaces.unitTests.servicesTests;
import mk.ukim.finki.wp.workspaces.model.domain.UserWorkspace;
import mk.ukim.finki.wp.workspaces.model.domain.Workspace;
import mk.ukim.finki.wp.workspaces.repository.UserWorkspaceRepository;
import mk.ukim.finki.wp.workspaces.repository.WorkspaceRepository;
import mk.ukim.finki.wp.workspaces.service.domain.UserWorkspaceService;
import mk.ukim.finki.wp.workspaces.service.domain.impl.WorkspaceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class WorkspaceServiceImplTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private UserWorkspaceRepository userWorkspaceRepository;

    @Mock
    private UserWorkspaceService userWorkspaceService;

    @InjectMocks
    private WorkspaceServiceImpl workspaceService;

    private Workspace testWorkspace;
    private UserWorkspace testUserWorkspace;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setting up test data
        testWorkspace = new Workspace("Test Workspace", "Test Description");
        testWorkspace.setId(1L);

        testUserWorkspace = new UserWorkspace();
        testUserWorkspace.setId(1L);
        testUserWorkspace.setWorkspace(testWorkspace);
        testUserWorkspace.setRole(mk.ukim.finki.wp.workspaces.model.enumerations.Role.ROLE_ADMIN);
    }

    // Test for findAll()
    @Test
    void testFindAll() {
        // Arrange
        when(workspaceRepository.findAll()).thenReturn(List.of(testWorkspace));

        // Act
        var workspaces = workspaceService.findAll();

        // Assert
        assertNotNull(workspaces);
        assertFalse(workspaces.isEmpty());
        assertEquals(1, workspaces.size());
        verify(workspaceRepository, times(1)).findAll();
    }

    // Test for openWorkspace()
    @Test
    void testOpenWorkspace_Success() {
        // Arrange
        Long workspaceId = 1L;
        Long userId = 1L;

        when(userWorkspaceRepository.findByWorkspaceIdAndUserId(workspaceId, userId))
                .thenReturn(Optional.of(testUserWorkspace));
        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(testWorkspace));

        // Act
        Optional<Workspace> workspace = workspaceService.openWorkspace(workspaceId, userId);

        // Assert
        assertTrue(workspace.isPresent());
        assertEquals("Test Workspace", workspace.get().getName());
        verify(userWorkspaceRepository, times(1)).findByWorkspaceIdAndUserId(workspaceId, userId);
        verify(workspaceRepository, times(1)).findById(workspaceId);
    }

    @Test
    void testOpenWorkspace_WorkspaceNotFound() {
        // Arrange
        Long workspaceId = 1L;
        Long userId = 1L;

        when(userWorkspaceRepository.findByWorkspaceIdAndUserId(workspaceId, userId))
                .thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            workspaceService.openWorkspace(workspaceId, userId);
        });
        assertEquals("Workspace not found", exception.getMessage());
        verify(userWorkspaceRepository, times(1)).findByWorkspaceIdAndUserId(workspaceId, userId);
        verify(workspaceRepository, times(0)).findById(workspaceId);
    }

    // Test for editWorkspace()
    @Test
    void testEditWorkspace_Success() {
        // Arrange
        Long workspaceId = 1L;
        Long userId = 1L;
        Workspace updatedWorkspace = new Workspace("Updated Workspace", "Updated description");

        when(userWorkspaceRepository.findByWorkspaceIdAndUserId(workspaceId, userId))
                .thenReturn(Optional.of(testUserWorkspace));
        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(testWorkspace));
        when(workspaceRepository.save(any(Workspace.class))).thenReturn(testWorkspace);

        // Act
        Optional<Workspace> updated = workspaceService.editWorkspace(workspaceId, userId, updatedWorkspace);

        // Assert
        assertTrue(updated.isPresent());
        assertEquals("Updated Workspace", updated.get().getName());
        assertEquals("Updated description", updated.get().getDescription());
        verify(userWorkspaceRepository, times(1)).findByWorkspaceIdAndUserId(workspaceId, userId);
        verify(workspaceRepository, times(1)).findById(workspaceId);
        verify(workspaceRepository, times(1)).save(any(Workspace.class));
    }

    @Test
    void testEditWorkspace_WorkspaceNotFound() {
        // Arrange
        Long workspaceId = 1L;
        Long userId = 1L;
        Workspace updatedWorkspace = new Workspace("Updated Workspace", "Updated description");

        when(userWorkspaceRepository.findByWorkspaceIdAndUserId(workspaceId, userId))
                .thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            workspaceService.editWorkspace(workspaceId, userId, updatedWorkspace);
        });
        assertEquals("Workspace not found", exception.getMessage());
        verify(userWorkspaceRepository, times(1)).findByWorkspaceIdAndUserId(workspaceId, userId);
        verify(workspaceRepository, times(0)).findById(workspaceId);
        verify(workspaceRepository, times(0)).save(any(Workspace.class));
    }

    // Test for findById()
    @Test
    void testFindById_Success() {
        // Arrange
        Long workspaceId = 1L;
        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(testWorkspace));

        // Act
        Optional<Workspace> workspace = workspaceService.findById(workspaceId);

        // Assert
        assertTrue(workspace.isPresent());
        assertEquals(workspaceId, workspace.get().getId());
        verify(workspaceRepository, times(1)).findById(workspaceId);
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        Long workspaceId = 1L;
        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.empty());

        // Act
        Optional<Workspace> workspace = workspaceService.findById(workspaceId);

        // Assert
        assertTrue(workspace.isEmpty());
        verify(workspaceRepository, times(1)).findById(workspaceId);
    }

    // Test for findAllPerUser()
    @Test
    void testFindAllPerUser() {
        // Arrange
        Long userId = 1L;
        when(userWorkspaceRepository.findAllByUserId(userId)).thenReturn(List.of(testUserWorkspace));

        // Act
        List<UserWorkspace> userWorkspaces = workspaceService.findAllPerUser(userId);

        // Assert
        assertNotNull(userWorkspaces);
        assertFalse(userWorkspaces.isEmpty());
        assertEquals(1, userWorkspaces.size());
        verify(userWorkspaceRepository, times(1)).findAllByUserId(userId);
    }
}
