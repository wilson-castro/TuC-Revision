package com.projeto.pi.projeto_pi.modals.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefreshTokenRequestDTO {
    @NotBlank(message = "O token deve ser informado")
    @NotNull(message = "O token deve ser informado")
    private String token;
}
