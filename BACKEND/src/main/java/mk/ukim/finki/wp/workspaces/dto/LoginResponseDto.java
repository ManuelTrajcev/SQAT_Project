package mk.ukim.finki.wp.workspaces.dto;

public record LoginResponseDto(
        String token
) {
    public String getToken() {
        return token;
    }
}
