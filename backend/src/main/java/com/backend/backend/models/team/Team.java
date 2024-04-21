package com.backend.backend.models.team;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Times")
@Entity(name = "times")
@EqualsAndHashCode(of = "id")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private Boolean ativo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dataCadastro = new Date();

    public Team(TeamResponseDTO data) {
        this.id = data.getId();
        this.nome = data.getNome();
        this.ativo = data.getAtivo();
        this.dataCadastro = data.getDataCadastro();
    }

    public TeamResponseDTO toDTO() {
        return TeamResponseDTO.from(this);
    }

    public boolean isEnabled() {
        if (this.ativo == null)
            return false;
        return true;
    }
}
