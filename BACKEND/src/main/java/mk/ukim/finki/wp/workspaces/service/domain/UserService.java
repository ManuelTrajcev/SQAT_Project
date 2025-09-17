package mk.ukim.finki.wp.workspaces.service.domain;

import mk.ukim.finki.wp.workspaces.model.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User register(String username, String email, String password, String repeatPassword);

    User login(String username, String password);

    User findByUsername(String username);

    boolean deleteUserByUsername(String username);
}
