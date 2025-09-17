package mk.ukim.finki.wp.workspaces.unitTests.repositoryTests;

import mk.ukim.finki.wp.workspaces.model.domain.User;
import mk.ukim.finki.wp.workspaces.model.domain.UserWorkspace;
import mk.ukim.finki.wp.workspaces.model.domain.Workspace;
import mk.ukim.finki.wp.workspaces.model.enumerations.Role;
import mk.ukim.finki.wp.workspaces.repository.UserWorkspaceRepository;
import mk.ukim.finki.wp.workspaces.repository.UserRepository;
import mk.ukim.finki.wp.workspaces.repository.WorkspaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@Transactional
public class UserWorkspaceRepositoryTests {

    @Autowired
    private UserWorkspaceRepository userWorkspaceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    private User testUser;
    private Workspace testWorkspace;
    private UserWorkspace testUserWorkspace;

    @BeforeEach
    public void setUp() {
        // Set up test data
        testUser = new User("testuser", "testemail@example.com", "testpassword");
        testWorkspace = new Workspace("Test Workspace", "Description of test workspace");

        // Save the entities
        userRepository.save(testUser);
        workspaceRepository.save(testWorkspace);

        // Create and save the UserWorkspace entity
        testUserWorkspace = new UserWorkspace(testUser, testWorkspace, Role.ROLE_ADMIN);
        userWorkspaceRepository.save(testUserWorkspace);
    }

    @AfterEach
    public void cleanUp() {
        // Delete the test entities after each test method
        userWorkspaceRepository.delete(testUserWorkspace);
        workspaceRepository.delete(testWorkspace);
        userRepository.delete(testUser);
    }

    @Test
    public void testFindByWorkspaceIdAndUserId() {
        // Given: We already have testUser and testWorkspace created and saved in setUp()

        // When: We try to find the UserWorkspace by workspaceId and userId
        Optional<UserWorkspace> foundUserWorkspace = userWorkspaceRepository.findByWorkspaceIdAndUserId(testWorkspace.getId(), testUser.getId());

        // Then: The UserWorkspace should be found and match the given IDs
        assertThat(foundUserWorkspace).isPresent();
        assertThat(foundUserWorkspace.get().getUser().getId()).isEqualTo(testUser.getId());
        assertThat(foundUserWorkspace.get().getWorkspace().getId()).isEqualTo(testWorkspace.getId());
    }

    @Test
    public void testFindByWorkspaceIdAndUserId_NotFound() {
        // Given: We use IDs that don't exist in the database
        Long nonExistentWorkspaceId = 999L;
        Long nonExistentUserId = 999L;

        // When: We try to find the UserWorkspace by non-existent workspaceId and userId
        Optional<UserWorkspace> foundUserWorkspace = userWorkspaceRepository.findByWorkspaceIdAndUserId(nonExistentWorkspaceId, nonExistentUserId);

        // Then: No UserWorkspace should be found
        assertThat(foundUserWorkspace).isNotPresent();
    }

    @Test
    public void testFindAllByUserId() {
        // Given: We have testUser with associated UserWorkspace

        // When: We try to find all UserWorkspace entries by userId
        List<UserWorkspace> userWorkspaces = userWorkspaceRepository.findAllByUserId(testUser.getId());

        // Then: The list of UserWorkspace should not be empty and should contain the created UserWorkspace
        assertThat(userWorkspaces).isNotEmpty();
        assertThat(userWorkspaces).hasSize(1); // Since we created one UserWorkspace
        assertThat(userWorkspaces.get(0).getUser().getId()).isEqualTo(testUser.getId());
    }

    @Test
    public void testFindAllByUserId_NotFound() {
        // Given: A user ID that doesn't have any associated UserWorkspace
        Long nonExistentUserId = 999L;

        // When: We try to find all UserWorkspace entries for a non-existent userId
        List<UserWorkspace> userWorkspaces = userWorkspaceRepository.findAllByUserId(nonExistentUserId);

        // Then: The list of UserWorkspace should be empty
        assertThat(userWorkspaces).isEmpty();
    }

    @Test
    public void testDeleteUserWorkspace() {
        // Given: The UserWorkspace testUserWorkspace exists in the repository
        Long userWorkspaceId = testUserWorkspace.getId();

        // When: We delete the UserWorkspace by its ID
        userWorkspaceRepository.deleteById(userWorkspaceId);

        // Then: The UserWorkspace should be deleted and no longer found in the repository
        Optional<UserWorkspace> deletedUserWorkspace = userWorkspaceRepository.findById(userWorkspaceId);
        assertThat(deletedUserWorkspace).isNotPresent();
    }

    @Test
    public void testDeleteNonExistentUserWorkspace() {
        // Given: An ID for a UserWorkspace that doesn't exist
        Long nonExistentUserWorkspaceId = 999L;

        // When: We try to delete the non-existent UserWorkspace
        userWorkspaceRepository.deleteById(nonExistentUserWorkspaceId);

        // Then: The UserWorkspace should still not be present in the repository
        Optional<UserWorkspace> deletedUserWorkspace = userWorkspaceRepository.findById(nonExistentUserWorkspaceId);
        assertThat(deletedUserWorkspace).isNotPresent();
    }
}
