package com.projeto.pi.projeto_pi.modals.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDTO {

    private String token;
    private Long exp;
    private String refresh_token;
    private Long exp_refresh;
    private String role;
    private Long userId;
    private String user;
    private String name;
    private String login;

}
