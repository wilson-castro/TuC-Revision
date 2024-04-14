package io.github.wilsoncastro.tucrevision.core.service;

import io.github.wilsoncastro.tucrevision.core.model.User;
import io.github.wilsoncastro.tucrevision.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public boolean existsByEmailEqualsIgnoreCase(String email) {
        return userRepository.existsByEmailEqualsIgnoreCase(email);
    }

    public boolean existsByEmailEqualsIgnoreCaseAndId(String email, Long id) {
        return userRepository.existsByEmailEqualsIgnoreCaseAndIdIsNot(email, id);
    }

    public User createUser(String name, String email, String password) {
        if (existsByEmailEqualsIgnoreCase(email.toLowerCase()))
            throw new IllegalArgumentException("E-mail já cadastrado. Por favor, tente outro.");

        var passwordCrypt = encoder.encode(password);

        return userRepository.save(new User(name, email, passwordCrypt, true));
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public User findByID(Long id) {
        return userRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("Usuário não encontrado."));
    }

    public Page<User> findAll(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size));
    }

}
