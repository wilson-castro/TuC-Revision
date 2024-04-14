package com.projeto.pi.projeto_pi.modals.interests;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InterestResponseDTO {

    private Long id;

    private Date dataInteresse;

    private String nome;

    private String telefone;

    private boolean ativo = false;

    public InterestResponseDTO(Interest interest) {
        this(interest.getId(), interest.getDataInteresse(), interest.getNome(), interest.getTelefone(),
                interest.isAtivo());
    }
}
