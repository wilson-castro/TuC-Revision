package com.backend.backend.models.agreement;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class AgreementResponseDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @JsonProperty("abstract")
    private String abst;

    private Long year;
    private String authors;
    private String journal;
    private String documentType;
    private String pages;
    private Long volume;
    private String url;
    private String affiliation;
    private String publisher;
    private String language;
    private String note;
    private Date dataCadastro;

    public AgreementResponseDTO(Agreement object) {
        this.id = object.getId();
        this.title = object.getTitle();
        this.abst = object.getAbst();
        this.year = object.getYear();
        this.authors = object.getAuthors();
        this.journal = object.getJournal();
        this.documentType = object.getDocumentType();
        this.pages = object.getPages();
        this.volume = object.getVolume();
        this.url = object.getUrl();
        this.affiliation = object.getAffiliation();
        this.publisher = object.getPublisher();
        this.language = object.getLanguage();
        this.note = object.getNote();
        this.dataCadastro = object.getDataCadastro();
    }

}
