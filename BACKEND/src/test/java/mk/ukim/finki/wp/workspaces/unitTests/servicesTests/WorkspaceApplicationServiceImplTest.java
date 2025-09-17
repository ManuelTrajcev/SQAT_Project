package mk.ukim.finki.wp.workspaces.unitTests.servicesTests;

import mk.ukim.finki.wp.workspaces.dto.DisplayWorkspaceDto;
import mk.ukim.finki.wp.workspaces.dto.EditWorkspaceDto;
import mk.ukim.finki.wp.workspaces.dto.WorkspaceWithRoleDto;
import mk.ukim.finki.wp.workspaces.model.domain.UserWorkspace;
import mk.ukim.finki.wp.workspaces.model.domain.Workspace;
import mk.ukim.finki.wp.workspaces.model.enumerations.Role;
import mk.ukim.finki.wp.workspaces.service.application.impl.WorkspaceApplicationServiceImpl;
import mk.ukim.finki.wp.workspaces.service.domain.WorkspaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkspaceApplicationServiceImplTest {

    @Mock
    private WorkspaceService workspaceService;

    @InjectMocks
    private WorkspaceApplicationServiceImpl workspaceApplicationService;

    private Workspace workspace;
    private UserWorkspace userWorkspace;
    private DisplayWorkspaceDto displayWorkspaceDto;
    private EditWorkspaceDto editWorkspaceDto;
    private WorkspaceWithRoleDto workspaceWithRoleDto;

    @BeforeEach
    public void setUp() {
        // Initialize mock Workspace, DTOs, and other objects
        workspace = new Workspace();
        workspace.setId(1L);
        workspace.setName("Test Workspace");
        workspace.setDescription("Test description");
        displayWorkspaceDto = DisplayWorkspaceDto.from(workspace);

        editWorkspaceDto = new EditWorkspaceDto("Updated Workspace Name", "Updated description", true);

        userWorkspace = new UserWorkspace();
        userWorkspace.setWorkspace(workspace);
        userWorkspace.setRole(Role.ROLE_ADMIN);

        workspaceWithRoleDto = WorkspaceWithRoleDto.from(userWorkspace);
    }

    @Test
    public void testFindAll() {
        // Arrange: Mock the findAll() method of the workspaceService
        when(workspaceService.findAll()).thenReturn(List.of(workspace));

        // Act: Call the findAll method of the service
        List<DisplayWorkspaceDto> result = workspaceApplicationService.findAll();

        // Assert: Verify the result contains the correct data
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Workspace", result.get(0).getName());
        assertEquals("Test description", result.get(0).getDescription());
        verify(workspaceService, times(1)).findAll();
    }

    @Test
    public void testOpenWorkspace() {
        // Arrange: Mock the openWorkspace() method of the workspaceService
        when(workspaceService.openWorkspace(1L, 1L)).thenReturn(Optional.of(workspace));

        // Act: Call the openWorkspace method of the service
        Optional<DisplayWorkspaceDto> result = workspaceApplicationService.openWorkspace(1L, 1L);

        // Assert: Verify the result is present and contains the correct workspace data
        assertTrue(result.isPresent());
        assertEquals("Test Workspace", result.get().getName());
        assertEquals("Test description", result.get().getDescription());
        verify(workspaceService, times(1)).openWorkspace(1L, 1L);
    }

    @Test
    public void testOpenWorkspaceThrowsException() {
        // Arrange: Mock the openWorkspace() method to return an empty Optional
        when(workspaceService.openWorkspace(1L, 1L)).thenReturn(Optional.empty());

        // Act & Assert: Call openWorkspace and assert that a RuntimeException is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            workspaceApplicationService.openWorkspace(1L, 1L);
        });

        assertEquals("Workspace not found", exception.getMessage());
        verify(workspaceService, times(1)).openWorkspace(1L, 1L);
    }

    @Test
    void testEditWorkspace() {
        // Create the Workspace object with any data for testing
        Workspace updatedWorkspace = new Workspace("Updated Workspace Name", "Updated description");

        // Stubbing with argument matchers (e.g., matching any Workspace object)
        when(workspaceService.editWorkspace(eq(1L), eq(1L), any(Workspace.class)))
                .thenReturn(Optional.of(updatedWorkspace));

        // Create the EditWorkspaceDto and call the service method
        EditWorkspaceDto editWorkspaceDto = new EditWorkspaceDto("Updated Workspace Name", "Updated description", true);

        Optional<EditWorkspaceDto> result = workspaceApplicationService.editWorkspace(1L, 1L, editWorkspaceDto);

        // Assert the result
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Updated Workspace Name");
        assertThat(result.get().getDescription()).isEqualTo("Updated description");

        // Verify the interaction
        verify(workspaceService).editWorkspace(eq(1L), eq(1L), any(Workspace.class));
    }

    @Test
    void testEditWorkspaceThrowsException() {
        // Given
        EditWorkspaceDto editWorkspaceDto = new EditWorkspaceDto("Updated Workspace",  "Updated description", false);

        // When / Then: Expect an exception to be thrown
        assertThrows(RuntimeException.class, () -> {
            workspaceApplicationService.editWorkspace(1L, 1L, editWorkspaceDto);
        });

        // Optionally verify the interaction with the service
        verify(workspaceService, times(1)).editWorkspace(eq(1L), eq(1L), any(Workspace.class));
    }

    @Test
    public void testFindAllPerUser() {
        // Arrange: Mock the findAllPerUser() method of the workspaceService
        // Correctly mock the return type to List<UserWorkspace>
        when(workspaceService.findAllPerUser(1L)).thenReturn(List.of(userWorkspace));

        // Act: Call the findAllPerUser method of the service
        List<WorkspaceWithRoleDto> result = workspaceApplicationService.findAllPerUser(1L);

        // Assert: Verify the result contains the correct data
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Workspace", result.get(0).getName());
        assertEquals("ROLE_ADMIN", result.get(0).getRole());
        verify(workspaceService, times(1)).findAllPerUser(1L);
    }
}
