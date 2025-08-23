package mk.ukim.finki.wp.workspaces.dto;

import mk.ukim.finki.wp.workspaces.model.domain.User;

public record DisplayUserDto(String username, String email) {
    public static DisplayUserDto from(User user) {
        return new DisplayUserDto(
                user.getUsername(),
                user.getEmail()
        );
    }

    public User toUser() {
        return new User(username, email);
    }
}
