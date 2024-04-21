package com.backend.backend.models.agreement;

import org.springframework.web.multipart.MultipartFile;

import com.backend.backend.models.DTOGenerator;
import com.backend.backend.utils.Csv;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.HashMap;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgreementRequestDTO {

    @NotBlank(message = "\"title\" deve ser informado")
    @NotNull(message = "\"title\" deve ser informado")
    private String title;

    @NotBlank(message = "\"abst\" deve ser informado")
    @NotNull(message = "\"abst\" deve ser informado")

    @JsonProperty("abstract")
    private String abst;

    @NotBlank(message = "\"year\" deve ser informado")
    @NotNull(message = "\"year\" deve ser informado")
    private Long year;

    @NotBlank(message = "\"authors\" deve ser informado")
    @NotNull(message = "\"authors\" deve ser informado")
    private String authors;

    @NotBlank(message = "\"journal\" deve ser informado")
    @NotNull(message = "\"journal\" deve ser informado")
    private String journal;

    @NotBlank(message = "\"documentType\" deve ser informado")
    @NotNull(message = "\"documentType\" deve ser informado")
    private String documentType;

    @NotBlank(message = "\"pages\" deve ser informado")
    @NotNull(message = "\"pages\" deve ser informado")
    private String pages;

    @NotBlank(message = "\"volume\" deve ser informado")
    @NotNull(message = "\"volume\" deve ser informado")
    private Long volume;

    @NotBlank(message = "\"url\" deve ser informado")
    @NotNull(message = "\"url\" deve ser informado")
    private String url;

    @NotBlank(message = "\"affiliation\" deve ser informado")
    @NotNull(message = "\"affiliation\" deve ser informado")
    private String affiliation;

    @NotBlank(message = "\"publisher\" deve ser informado")
    @NotNull(message = "\"publisher\" deve ser informado")
    private String publisher;

    @NotBlank(message = "\"language\" deve ser informado")
    @NotNull(message = "\"language\" deve ser informado")
    private String language;

    @NotBlank(message = "\"note\" deve ser informado")
    @NotNull(message = "\"note\" deve ser informado")
    private String note;

    public Agreement toEntity() {
        return DTOGenerator.from(this, new Agreement());
    }

    public static Agreement[] fromCSV(MultipartFile file) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        byte[] bytes = file.getBytes();
        List<HashMap<String, Object>> agreements = Csv.csvToJson(bytes);

        Agreement[] agreementsArray = new Agreement[agreements.size()];
        int i = 0;

        for (HashMap<String, Object> agreement : agreements) {
            Agreement agrrementObj = objectMapper.convertValue(agreement, Agreement.class);
            agreementsArray[i++] = agrrementObj;
        }
        return agreementsArray;
    }

    public static Agreement[] fromCSV(byte[] bytes) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        List<HashMap<String, Object>> agreements = Csv.csvToJson(bytes);

        Agreement[] agreementsArray = new Agreement[agreements.size()];
        int i = 0;

        for (HashMap<String, Object> agreement : agreements) {
            Agreement agrrementObj = objectMapper.convertValue(agreement, Agreement.class);
            agreementsArray[i++] = agrrementObj;
        }
        return agreementsArray;
    }

}
