package com.backend.backend.models.agreement;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
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
@Table(name = "concordancias")
@Entity(name = "concordancias")
@EqualsAndHashCode(of = "id")


public class Agreement {

    @Transient
    private static final int size = 1_000_000; // 1mb
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title",length = size)
    private String title;

    @Column(name = "abstract", length = size)
    @JsonProperty("abstract")
    private String abst;

    @Column(name = "year",length = size)
    private Long year;

    @Column(name = "authors", length = size)
    private String authors;

    @Column(name = "journal", length = size)
    private String journal;

    @Column(name = "document_type", length = size)
    private String documentType;

    @Column(name = "pages", length = size)
    private String pages;

    @Column(name = "volume", length = size)
    private Long volume;

    @Column(name = "url", length = size)
    private String url;

    @Column(name = "affiliation", length = size)
    private String affiliation;

    @Column(name = "publisher", length = size)
    private String publisher;

    @Column(name = "language", length = size)
    private String language;

    @Column(name = "note", length = size)
    private String note;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dataCadastro = new Date();

    public Agreement(AgreementResponseDTO data) {
        this.title = data.getTitle();
        this.abst = data.getAbst();
        this.year = data.getYear();
        this.authors = data.getAuthors();
        this.journal = data.getJournal();
        this.documentType = data.getDocumentType();
        this.pages = data.getPages();
        this.volume = data.getVolume();
        this.url = data.getUrl();
        this.affiliation = data.getAffiliation();
        this.publisher = data.getPublisher();
        this.language = data.getLanguage();
        this.note = data.getNote();
    }

    public AgreementResponseDTO toDTO() {
        return new AgreementResponseDTO(this);
    }

    public Agreement(AgreementRequestDTO agreementRequestDTO) {
        this.title = agreementRequestDTO.getTitle();
        this.abst = agreementRequestDTO.getAbst();
        this.year = agreementRequestDTO.getYear();
        this.authors = agreementRequestDTO.getAuthors();
        this.journal = agreementRequestDTO.getJournal();
        this.documentType = agreementRequestDTO.getDocumentType();
        this.pages = agreementRequestDTO.getPages();
        this.volume = agreementRequestDTO.getVolume();
        this.url = agreementRequestDTO.getUrl();
        this.affiliation = agreementRequestDTO.getAffiliation();
        this.publisher = agreementRequestDTO.getPublisher();
        this.language = agreementRequestDTO.getLanguage();
        this.note = agreementRequestDTO.getNote();
    }

}
