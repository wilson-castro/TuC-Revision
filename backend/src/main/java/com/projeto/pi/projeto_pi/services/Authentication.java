package com.projeto.pi.projeto_pi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.projeto.pi.projeto_pi.modals.users.UserRepo;

@Service
public class Authentication implements UserDetailsService {

    @Autowired
    UserRepo repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByLoginIgnoreCase(username).get();
    }

}
