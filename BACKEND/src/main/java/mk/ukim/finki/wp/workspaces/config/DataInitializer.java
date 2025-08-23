package mk.ukim.finki.wp.workspaces.config;

import jakarta.annotation.PostConstruct;
import mk.ukim.finki.wp.workspaces.model.domain.User;
import mk.ukim.finki.wp.workspaces.model.domain.UserWorkspace;
import mk.ukim.finki.wp.workspaces.model.domain.Workspace;
import mk.ukim.finki.wp.workspaces.model.enumerations.Role;
import mk.ukim.finki.wp.workspaces.repository.UserRepository;
import mk.ukim.finki.wp.workspaces.repository.UserWorkspaceRepository;
import mk.ukim.finki.wp.workspaces.repository.WorkspaceRepository;
import mk.ukim.finki.wp.workspaces.service.domain.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WorkspaceRepository workspaceRepository;
    private final UserWorkspaceRepository userWorkspaceRepository;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, WorkspaceRepository workspaceRepository, UserWorkspaceRepository userWorkspaceRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.workspaceRepository = workspaceRepository;
        this.userWorkspaceRepository = userWorkspaceRepository;
    }

    @PostConstruct
    void init() {
//        userRepository.deleteAll();
//        workspaceRepository.deleteAll();
//        userWorkspaceRepository.deleteAll();
//
//        User user1 = new User(
//               "mt",
//                "manuel.trajcev@gmail.com",
//                passwordEncoder.encode("mt"));
//        userRepository.save(user1);
//
//        Workspace workspace1 = new Workspace(
//                "workspace1",
//                "workspace1 description"
//        );
//        workspaceRepository.save(workspace1);
//
//        Workspace workspace2 = new Workspace(
//                "workspace2",
//                "workspace2 description"
//        );
//        workspaceRepository.save(workspace2);
//
//
//        Workspace workspace3 = new Workspace(
//                "workspace3",
//                "workspace3 description"
//        );
//        workspaceRepository.save(workspace3);
//
//        UserWorkspace userWorkspace1 = new UserWorkspace(
//                user1,
//                workspace1,
//                Role.ROLE_ADMIN
//        );
//
//        userWorkspaceRepository.save(userWorkspace1);
//
//        UserWorkspace userWorkspace2 = new UserWorkspace(
//                user1,
//                workspace2,
//                Role.ROLE_VISITOR
//        );
//
//        userWorkspaceRepository.save(userWorkspace2);
    }
}
