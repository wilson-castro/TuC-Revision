package com.projeto.pi.projeto_pi.modals.interests;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InterestRequestDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past(message = "A data de nascimento deve ser posterior a data atual")
    private Date dataInteresse;

    @NotNull(message = "O carro deve ser informado")
    private Long carId;

    @NotBlank(message = "O usuario deve ser informado")
    private String nome;

    @NotBlank(message = "O usuario deve ser informado")
    @Pattern(regexp = "(?:([+]\\d{1,4})[-.\\s]?)?(?:[(](\\d{1,3})[)][-.\\s]?)?(\\d{1,4})[-.\\s]?(\\d{1,4})[-.\\s]?(\\d{1,9})", message = "Telefone invalido")
    private String telefone;

    private Boolean ativo;

    public Interest toEntity() {
        if (ativo == null) {
            ativo = true;
        }
        if (dataInteresse == null) {
            dataInteresse = new Date();
        }

        return new Interest(id, dataInteresse, null, nome, telefone, ativo);
    }
}
