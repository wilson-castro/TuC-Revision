package com.backend.backend.models.team;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamRequestDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome deve ser informado")
    @NotNull(message = "Valor invalido para o nome")
    private String nome;

    private Boolean ativo = true;

    private Date dataCadastro = new Date();


    public Team toEntity() {

        // Create and return Team object

        return new Team(id, nome, ativo, dataCadastro);
    }

}
