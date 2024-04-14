package io.github.wilsoncastro.tucrevision.core.pojo;

public record UpdateUserDTO(
    String name,
    String email,
    String password
) {
}
