package com.projeto.pi.projeto_pi.modals.users;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.projeto.pi.projeto_pi.annotations.EnumValidator;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequestDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O login deve ser informado")
    @NotNull(message = "Valor invalido para o login")
    private String login;
    @NotBlank(message = "A senha deve ser informada")
    @NotNull(message = "Valor invalido para a senha")
    private String senha;
    @NotBlank(message = "O nome deve ser informado")
    @NotNull(message = "Valor invalido para o nome")
    private String nome;

    private Boolean ativo = true;

    @NotNull(message = "O data de nascimento deve ser informado")
    @Past(message = "A data de nascimento deve ser posterior a data atual")
    private Date dataNascimento;

    @NotNull(message = "O role deve ser informado")
    @EnumValidator(enumclass = User.Role.class, message = "Valor inválido para role")
    private String role;

    private Date dataCadastro = new Date();

    public User toEntity() {
        return new User(id, login, senha, nome, ativo, dataNascimento, dataCadastro, role);
    }

}
