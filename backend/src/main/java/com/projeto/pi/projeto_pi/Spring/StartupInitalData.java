package com.projeto.pi.projeto_pi.Spring;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.projeto.pi.projeto_pi.modals.users.User;
import com.projeto.pi.projeto_pi.modals.users.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class StartupInitalData implements CommandLineRunner {

    @Autowired
    UserRepo userRepository;
    @Autowired
    PasswordEncoder encoder;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        long id = 1;
        Date date = new Date(System.currentTimeMillis());
        // Set<Interest> interests = null;
        if (userRepository.findByLoginIgnoreCase(adminUsername).isEmpty()) {
            User admin = new User(id, adminUsername, encoder.encode(
                    adminPassword), "Administrador", true,
                    date, date, "ADMIN");
            userRepository.save(admin);
        }

    }
}
