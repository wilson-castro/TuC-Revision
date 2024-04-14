package com.projeto.pi.projeto_pi.modals.cars;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
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
@Table(name = "Carros")
@Entity(name = "carros")
@EqualsAndHashCode(of = "id")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;

    @Column(length = 2048)
    private String descricao;

    private String modelo;

    @JsonFormat(pattern = "yyyy")
    private Date anoModelo;

    @JsonFormat(pattern = "yyyy")
    private Date anoFabricacao;

    private double valor;
    
    @Column(length = 65_000)
    private String image;

    public Car(CarResponseDTO data) {
        this.marca = data.getMarca();
        this.descricao = data.getDescricao();
        this.modelo = data.getModelo();
        this.anoModelo = data.getAnoModelo();
        this.anoFabricacao = data.getAnoFabricacao();
        this.valor = data.getValor();
        this.image = data.getImage();
        this.id = data.getId();
    }

    public CarResponseDTO toDTO() {
        return new CarResponseDTO(this);
    }
}
