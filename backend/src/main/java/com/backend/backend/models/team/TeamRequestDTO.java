package com.backend.backend.models.team;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.backend.backend.models.users.User;
import com.backend.backend.models.users.UserRepo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamRequestDTO {

    @Transient
    @Autowired
    UserRepo userRepository;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome deve ser informado")
    @NotNull(message = "Valor invalido para o nome")
    private String nome;

    private Boolean ativo = true;

    private Date dataCadastro = new Date();

    @NotNull(message = "O usuário deve ser informado")
    private Long userId;

    public Team toEntity() {
        // Get user from database by id
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        // Create and return Team object

        return new Team(id, nome, ativo, dataCadastro, user.get());
    }

}
