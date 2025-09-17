package mk.ukim.finki.wp.workspaces.unitTests.repositoryTests;

import mk.ukim.finki.wp.workspaces.model.domain.Workspace;
import mk.ukim.finki.wp.workspaces.repository.WorkspaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@Transactional
public class WorkspaceRepositoryTests {

    @Autowired
    private WorkspaceRepository workspaceRepository;

    private Workspace testWorkspace;

    @BeforeEach
    public void setUp() {
        // Create and save a workspace for testing
        testWorkspace = new Workspace("Test Workspace", "Description of test workspace");
        workspaceRepository.save(testWorkspace);
    }

    @AfterEach
    public void cleanUp() {
        // Delete the test entity after each test method
        workspaceRepository.delete(testWorkspace);
    }

    @Test
    public void testSaveWorkspace() {
        // Given: A new workspace is created in the setup method
        Workspace newWorkspace = new Workspace("New Workspace", "Description of new workspace");

        // When: The workspace is saved
        Workspace savedWorkspace = workspaceRepository.save(newWorkspace);

        // Then: The workspace should have an ID and match the saved values
        assertThat(savedWorkspace).isNotNull();
        assertThat(savedWorkspace.getId()).isNotNull();
        assertThat(savedWorkspace.getName()).isEqualTo("New Workspace");
        assertThat(savedWorkspace.getDescription()).isEqualTo("Description of new workspace");
    }

    @Test
    public void testFindById() {
        // Given: A workspace is already saved in the setup method
        Long workspaceId = testWorkspace.getId();

        // When: We try to find the workspace by its ID
        Optional<Workspace> foundWorkspace = workspaceRepository.findById(workspaceId);

        // Then: The workspace should be present and match the ID
        assertThat(foundWorkspace).isPresent();
        assertThat(foundWorkspace.get().getId()).isEqualTo(workspaceId);
    }

    @Test
    public void testFindByIdNotFound() {
        // Given: An ID that doesn't exist
        Long nonExistentWorkspaceId = 999L;

        // When: We try to find the workspace by a non-existent ID
        Optional<Workspace> foundWorkspace = workspaceRepository.findById(nonExistentWorkspaceId);

        // Then: The workspace should not be found
        assertThat(foundWorkspace).isNotPresent();
    }

    @Test
    public void testDeleteWorkspace() {
        // Given: A workspace exists in the database
        Long workspaceId = testWorkspace.getId();

        // When: The workspace is deleted by its ID
        workspaceRepository.deleteById(workspaceId);

        // Then: The workspace should be deleted and not found in the repository
        Optional<Workspace> deletedWorkspace = workspaceRepository.findById(workspaceId);
        assertThat(deletedWorkspace).isNotPresent();
    }

    @Test
    public void testDeleteNonExistentWorkspace() {
        // Given: An ID for a workspace that does not exist
        Long nonExistentWorkspaceId = 999L;

        // When: We try to delete the non-existent workspace
        workspaceRepository.deleteById(nonExistentWorkspaceId);

        // Then: The workspace should not be found in the repository
        Optional<Workspace> deletedWorkspace = workspaceRepository.findById(nonExistentWorkspaceId);
        assertThat(deletedWorkspace).isNotPresent();
    }

    @Test
    public void testUpdateWorkspace() {
        // Given: A workspace exists in the database and we want to update it
        testWorkspace.setName("Updated Workspace Name");
        testWorkspace.setDescription("Updated description");

        // When: We save the updated workspace
        Workspace updatedWorkspace = workspaceRepository.save(testWorkspace);

        // Then: The workspace should be updated in the repository
        assertThat(updatedWorkspace).isNotNull();
        assertThat(updatedWorkspace.getName()).isEqualTo("Updated Workspace Name");
        assertThat(updatedWorkspace.getDescription()).isEqualTo("Updated description");
    }

    @Test
    public void testDeleteCreatedWorkspace() {
        // Given: User was created and saved in the @BeforeEach method
        Long workspaceId = testWorkspace.getId();

        // When: We delete the user by ID
        workspaceRepository.deleteById(workspaceId);

        // Then: The user should be deleted and no longer present in the repository
        Optional<Workspace> deletedUser = workspaceRepository.findById(workspaceId);
        assertThat(deletedUser).isNotPresent();
    }

}
