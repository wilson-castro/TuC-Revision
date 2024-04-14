package com.projeto.pi.projeto_pi.configuration;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Define o codificador padrão para valores
 * 
 * @return [PasswordEncoder]
 * @author Luis Ricardo Alves Santos
 * @since 1.0
 */
public class Encoder {

    /**
     * Retorna o codificador padrão
     */
    public static PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

}
