package mk.ukim.finki.wp.workspaces.dto;

public record CreateUserDto(
        String username,
        String email,
        String password,
        String repeatPassword
) {
}
