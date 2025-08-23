package mk.ukim.finki.wp.workspaces.model.enumerations;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN, ROLE_VISITOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
