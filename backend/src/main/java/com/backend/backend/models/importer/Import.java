package com.backend.backend.models.importer;

import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "importacoes")
@Entity(name = "importacoes")
@EqualsAndHashCode(of = "id")
public class Import {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Status status;

    private String message;

    public static enum Status {
        IDDLE("IDDLE"),
        STARTED("STARTED"),
        FINISHED("FINISHED"),
        ERROR("ERROR"),
        RUNNING("RUNNING");

        String value;

        Status(String string) {
            this.value = string;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
