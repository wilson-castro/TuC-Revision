package io.github.wilsoncastro.tucrevision.core.security;

import io.github.wilsoncastro.tucrevision.core.model.User;
import io.github.wilsoncastro.tucrevision.core.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Nenhum usuário encontrado com o email: " + email));
        return UserDetailsImpl.build(user);
    }
}

