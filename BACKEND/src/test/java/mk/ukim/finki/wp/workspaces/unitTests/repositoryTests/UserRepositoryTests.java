package mk.ukim.finki.wp.workspaces.unitTests.repositoryTests;

import mk.ukim.finki.wp.workspaces.model.domain.User;
import mk.ukim.finki.wp.workspaces.repository.UserRepository;
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
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    public void setUp() {
        // Initialize test data
        testUser = new User("testuser", "testemail@example.com", "testpassword");
        userRepository.save(testUser);
    }

    @AfterEach
    public void cleanUp() {
        // Delete the test entities after each test method
        userRepository.delete(testUser);
    }

    @Test
    public void testFindByUsernameAndPassword() {
        // Given
        String username = "testuser";
        String password = "testpassword";

        // When
        Optional<User> result = userRepository.findByUsernameAndPassword(username, password);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo(username);
        assertThat(result.get().getPassword()).isEqualTo(password);
    }

    @Test
    public void testFindByUsername() {
        // Given
        String username = "testuser";

        // When
        Optional<User> result = userRepository.findByUsername(username);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo(username);
    }

    @Test
    public void testFetchAll() {
        // When
        List<User> users = userRepository.fetchAll();

        // Then
        assertThat(users).isNotEmpty();
        assertThat(users).contains(testUser);
    }

    @Test
    public void testLoadAll() {
        // When
        List<User> users = userRepository.loadAll();

        // Then
        assertThat(users).isNotEmpty();
        assertThat(users.get(0).getUsername()).isEqualTo(testUser.getUsername());
    }

    @Test
    public void testFindByUsernameAndPasswordWithWrongCredentials() {
        // Given
        String username = "wronguser";
        String password = "wrongpassword";

        // When
        Optional<User> result = userRepository.findByUsernameAndPassword(username, password);

        // Then
        assertThat(result).isNotPresent();
    }

    @Test
    public void testDeleteUser() {
        // Given
        Long userId = testUser.getId();

        // When
        userRepository.deleteById(userId);

        // Then
        Optional<User> result = userRepository.findById(userId);
        assertThat(result).isNotPresent();
    }

    @Test
    public void testDeleteNonExistentUser() {
        // Given
        Long nonExistentUserId = 999L;

        // When / Then
        Optional<User> resultBeforeDelete = userRepository.findById(nonExistentUserId);
        assertThat(resultBeforeDelete).isNotPresent();

        // Perform delete operation (does nothing but also doesn't throw exception)
        userRepository.deleteById(nonExistentUserId);

        // Verify that nothing has changed
        Optional<User> resultAfterDelete = userRepository.findById(nonExistentUserId);
        assertThat(resultAfterDelete).isNotPresent();
    }

    @Test
    public void testSaveUser() {
        // Given
        User newUser = new User("newuser", "newuser@example.com", "newpassword");

        // When
        User savedUser = userRepository.save(newUser);

        // Then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("newuser");
    }

    @Test
    public void testUpdateUser() {
        // Given
        testUser.setUsername("updatedUser");
        testUser.setEmail("updatedEmail@example.com");

        // When
        User updatedUser = userRepository.save(testUser);

        // Then
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getUsername()).isEqualTo("updatedUser");
        assertThat(updatedUser.getEmail()).isEqualTo("updatedEmail@example.com");
    }

    @Test
    public void testFindByUsernameNotFound() {
        // Given
        String username = "nonexistentuser";

        // When
        Optional<User> result = userRepository.findByUsername(username);

        // Then
        assertThat(result).isNotPresent();
    }

    @Test
    public void testDeleteCreatedUser() {
        // Given: User was created and saved in the @BeforeEach method
        Long userId = testUser.getId();

        // When: We delete the user by ID
        userRepository.deleteById(userId);

        // Then: The user should be deleted and no longer present in the repository
        Optional<User> deletedUser = userRepository.findById(userId);
        assertThat(deletedUser).isNotPresent();
    }
}
