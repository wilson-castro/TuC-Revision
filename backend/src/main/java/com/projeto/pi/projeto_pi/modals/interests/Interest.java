package com.projeto.pi.projeto_pi.modals.interests;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projeto.pi.projeto_pi.modals.cars.Car;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "Interesses")
@Entity(name = "interesses")
@EqualsAndHashCode(of = "id")
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dataInteresse;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "carId", unique = false, nullable = false, updatable = false)
    private Car carro;

    private String nome;

    private String telefone;

    private boolean ativo = false;

    public InterestResponseDTO toDTO() {
        return new InterestResponseDTO(this);
    }

}
