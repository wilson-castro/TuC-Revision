package com.projeto.pi.projeto_pi.controller;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.projeto.pi.projeto_pi.modals.users.User;
import com.projeto.pi.projeto_pi.modals.users.UserRecoverDTO;
import com.projeto.pi.projeto_pi.modals.users.UserRepo;
import com.projeto.pi.projeto_pi.modals.users.UserRequestDTO;
import com.projeto.pi.projeto_pi.modals.users.UserResponseDTO;
import com.projeto.pi.projeto_pi.utils.ReplaceObjectAttributes;
import com.projeto.pi.projeto_pi.utils.ReponseBuilder;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserRepo repository;

    @Autowired
    PasswordEncoder encoder;

    ReponseBuilder er = new ReponseBuilder();

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        Optional<User> existingItemOptional = repository.findById(id);

        if (existingItemOptional.isPresent()) {
            User existingItem = existingItemOptional.get();

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) auth.getPrincipal();

            if (user.getId() != id && !user.getLogin().equalsIgnoreCase("admin")) {
                throw new AccessDeniedException("Acesso negado");
            }

            return new ResponseEntity<UserResponseDTO>(existingItem.toDTO(), HttpStatus.OK);
        } else {

            return er.error("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public Page<UserResponseDTO> getAll(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest of = PageRequest.of(page, size);

        Page<UserResponseDTO> findAll = repository.findAll(of).map(user -> user.toDTO());
        return findAll;
    }

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<?> create(@RequestBody @Valid UserRequestDTO item) {
        try {
            if (item.getRole() != null) {
                item.setRole(item.getRole().toUpperCase()); /// Correção para que o role sempre seja em maisculo
            }

            User itemToBeSaved = item.toEntity();
            boolean exists = repository.findByLoginIgnoreCase(itemToBeSaved.getLogin()).isEmpty();
            if (!exists) {
                return er.error("Usuário já existente", HttpStatus.CONFLICT);
            }
            itemToBeSaved.setSenha(encoder.encode(itemToBeSaved.getSenha()));
            User savedItem = repository.save(itemToBeSaved);
            return new ResponseEntity<>(savedItem.toDTO(), HttpStatus.CREATED);
        } catch (Exception e) {

            return er.error("Erro ao criar usuário:" + e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        Optional<User> existingItemOptional = repository.findById(id);

        if (existingItemOptional.isPresent()) {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return er.error("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody UserRequestDTO item) {
        Optional<User> existingItemOptional = repository.findById(id);

        if (existingItemOptional.isPresent()) {
            User existingItem = existingItemOptional.get();

            if (item.getRole() != null) {
                item.setRole(item.getRole().toUpperCase());
            } /// Correção para que o role sempre seja em maisculo

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) auth.getPrincipal();

            if (user.getId() != id && !user.getLogin().equalsIgnoreCase("admin")) {
                throw new AccessDeniedException("Acesso negado");
            }

            ReplaceObjectAttributes<User> rep = new ReplaceObjectAttributes<>(existingItem);
            rep.replaceWith(item.toEntity());

            UserResponseDTO savedItem = repository.save(existingItem).toDTO();

            return new ResponseEntity<>(savedItem, HttpStatus.OK);
        } else {
            return er.error("Usuario não encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
    public ResponseEntity<?> recover(@RequestBody UserRecoverDTO item) {

        if (item.getSenha() == null) {
            return er.error("Senha não informada", HttpStatus.BAD_REQUEST);
        }
        if (item.getDataNascimento() == null) {
            return er.error("Data de nascimento não informada", HttpStatus.BAD_REQUEST);
        }

        Date data = Date.from(item.getDataNascimento().toInstant().plus(3, ChronoUnit.HOURS));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dataFormatada = sdf.format(data);

        Optional<User> existingItemOptional = repository.findToRecover(item.getLogin(),
                dataFormatada);

        if (existingItemOptional.isPresent()) {
            User existingItem = existingItemOptional.get();
            existingItem.setSenha(item.getSenha());

            ReplaceObjectAttributes<User> rep = new ReplaceObjectAttributes<>(existingItem);

            rep.replaceWith(existingItem);

            UserResponseDTO savedItem = repository.save(existingItem).toDTO();

            return new ResponseEntity<>(savedItem, HttpStatus.OK);
        } else {
            return er.error("Usuario não encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<?> handleAccessException(Exception ex, WebRequest request) {
        return er.error("Acesso negado. Nivel de permissão insuficiente", HttpStatus.FORBIDDEN);
    }

}
