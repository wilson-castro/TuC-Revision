package com.backend.backend.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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

import com.backend.backend.models.team.Team;
import com.backend.backend.models.team.TeamRepo;
import com.backend.backend.models.team.TeamRequestDTO;
import com.backend.backend.models.team.TeamResponseDTO;
import com.backend.backend.utils.ReplaceObjectAttributes;
import com.backend.backend.utils.ReponseBuilder;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/teams")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TeamController {

    @Autowired
    private TeamRepo repository;

    @Autowired
    PasswordEncoder encoder;

    ReponseBuilder er = new ReponseBuilder();

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Team> existingItemOptional = repository.findById(id);

        if (existingItemOptional.isPresent()) {
            Team existingItem = existingItemOptional.get();

            return new ResponseEntity<TeamResponseDTO>(existingItem.toDTO(), HttpStatus.OK);
        } else {

            return er.error("Time não encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public Page<TeamResponseDTO> getAll(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest of = PageRequest.of(page, size);

        Page<TeamResponseDTO> findAll = repository.findAll(of).map(user -> user.toDTO());
        return findAll;
    }

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<?> create(@RequestBody @Valid TeamRequestDTO item) {
        try {
            Team itemToBeSaved = item.toEntity();
            Team savedItem = repository.save(itemToBeSaved);
            return new ResponseEntity<>(savedItem.toDTO(), HttpStatus.CREATED);
        } catch (Exception e) {

            return er.error("Erro ao criar usuário:" + e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Team> existingItemOptional = repository.findById(id);

        if (existingItemOptional.isPresent()) {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return er.error("Time não encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody TeamRequestDTO item) {
        Optional<Team> existingItemOptional = repository.findById(id);

        if (existingItemOptional.isPresent()) {
            Team existingItem = existingItemOptional.get();


            ReplaceObjectAttributes<Team> rep = new ReplaceObjectAttributes<>(existingItem);
            rep.replaceWith(item.toEntity());

            TeamResponseDTO savedItem = repository.save(existingItem).toDTO();

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
