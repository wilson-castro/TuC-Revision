package com.backend.backend.modals.users;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRecoverDTO {
    private String login;
    private String senha;
    private Date dataNascimento;
}
