package io.github.wilsoncastro.tucrevision.core.pojo;

public record LoggedUserDTO(
        Long id,
        String name,
        String email,
        String token
) {
}
