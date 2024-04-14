package com.projeto.pi.projeto_pi.modals.cars;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarResponseDTO {

    Long id;
    String marca;
    String modelo;

    @JsonFormat(pattern = "yyyy")
    Date anoFabricacao;

    @JsonFormat(pattern = "yyyy")
    Date anoModelo;

    Double valor;
    String descricao;
    String image;

    public CarResponseDTO(Car car) {
        this(car.getId(), car.getMarca(), car.getModelo(), car.getAnoFabricacao(), car.getAnoModelo(),
                car.getValor(), car.getDescricao(), car.getImage());
    }

}
