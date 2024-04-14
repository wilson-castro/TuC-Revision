package com.projeto.pi.projeto_pi.modals.users;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projeto.pi.projeto_pi.annotations.Encode;

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
@Table(name = "Usuarios")
@Entity(name = "usuarios")
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;

    @Encode
    private String senha;

    private String nome;

    private Boolean ativo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dataNascimento;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dataCadastro = new Date();

    public static enum Role {
        ADMIN,
        USER
    }

    private String role = "USER";

    public User(UserResponseDTO data) {
        this.id = data.getId();
        this.login = data.getLogin();
        this.senha = data.getSenha();
        this.nome = data.getNome();
        this.ativo = data.getAtivo();
        this.dataNascimento = data.getDataNascimento();
        this.dataCadastro = data.getDataCadastro();

    }

    public String getRole() {
        return this.role;
    }

    public UserResponseDTO toDTO() {
        return new UserResponseDTO(this);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = this.role;
        if (this.role != null)
            role = "ROLE_" + this.role.toUpperCase();

        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {

        return senha;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;

    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if (this.ativo == null)
            return false;

        return true;
    }
}
