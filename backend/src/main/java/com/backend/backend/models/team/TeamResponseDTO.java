package com.backend.backend.models.team;

import java.util.Date;

import com.backend.backend.models.DTOGenerator;
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

    public TeamResponseDTO() {

    }


    public static TeamResponseDTO from(Team object) {
        TeamResponseDTO from = DTOGenerator.from(object, new TeamResponseDTO());
        return from;
    }

}
