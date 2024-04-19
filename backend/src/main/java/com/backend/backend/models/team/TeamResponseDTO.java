package com.backend.backend.models.team;

import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class TeamResponseDTO {

    private Long id;
    private String nome;
    private Boolean ativo;
    private Date dataCadastro;

    public TeamResponseDTO(Team team) {
        this(team.getId(), team.getNome(), team.getAtivo(),
        team.getDataCadastro());
    }

}
