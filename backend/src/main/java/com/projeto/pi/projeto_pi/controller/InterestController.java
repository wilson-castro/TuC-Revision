package com.projeto.pi.projeto_pi.controller;

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

import com.projeto.pi.projeto_pi.modals.cars.Car;
import com.projeto.pi.projeto_pi.modals.cars.CarRepo;
import com.projeto.pi.projeto_pi.modals.interests.Interest;
import com.projeto.pi.projeto_pi.modals.interests.InterestRepo;
import com.projeto.pi.projeto_pi.modals.interests.InterestRequestDTO;
import com.projeto.pi.projeto_pi.modals.interests.InterestResponseDTO;
import com.projeto.pi.projeto_pi.modals.users.User;
import com.projeto.pi.projeto_pi.utils.ReplaceObjectAttributes;
import com.projeto.pi.projeto_pi.utils.ReponseBuilder;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/interests")
public class InterestController {

    @Autowired
    private InterestRepo repository;

    @Autowired
    CarRepo carRepo;

    @Autowired
    PasswordEncoder encoder;

    ReponseBuilder er = new ReponseBuilder();

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        Optional<Interest> existingItemOptional = repository.findById(id);

        if (existingItemOptional.isPresent()) {
            Interest existingItem = existingItemOptional.get();

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) auth.getPrincipal();

            if (!user.getLogin().equalsIgnoreCase("admin")) {
                throw new AccessDeniedException("Acesso negado");
            }

            return new ResponseEntity<>(existingItem, HttpStatus.OK);
        } else {

            return er.error("Interesse não encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public Page<?> getAll(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "") String nome) {
        PageRequest of = PageRequest.of(page, size);

        if (nome.isEmpty()) {
            Page<Interest> findAll = repository.findAll(of);
            return findAll;
        }

        Page<Interest> findAll = repository.findAllByNome(nome, of);
        return findAll;
    }

    @PostMapping
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> create(@RequestBody @Valid InterestRequestDTO item) {
        try {

            Interest itemToBeSaved = item.toEntity();
            Optional<Car> optCarro = carRepo.findById(item.getCarId());

            if (optCarro.isEmpty()) {
                return er.error("O carro informado não existe", HttpStatus.BAD_REQUEST);
            }

            Car carro = optCarro.get();
            Optional<Interest> findByCarro = repository.findByCarId(carro.getId());
            boolean exists = findByCarro.isPresent();
            if (exists) {
                return er.error("O carro já possui um usuário interessado", HttpStatus.CONFLICT);
            }
            itemToBeSaved.setCarro(carro);

            Interest savedItem = repository.save(itemToBeSaved);

            InterestResponseDTO dto = new InterestResponseDTO(savedItem);

            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {

            return er.error("Erro ao criar interesse:" + e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        Optional<Interest> existingItemOptional = repository.findById(id);

        if (existingItemOptional.isPresent()) {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return er.error("Interesse não encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody InterestRequestDTO item) {
        Optional<Interest> existingItemOptional = repository.findById(id);

        if (existingItemOptional.isPresent()) {
            Interest existingItem = existingItemOptional.get();
            Interest requestItem = item.toEntity();

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) auth.getPrincipal();

            if (requestItem.getNome() != existingItem.getNome() && !user.getLogin().equalsIgnoreCase("admin")) {
                throw new AccessDeniedException("Acesso negado");
            }

            ReplaceObjectAttributes<Interest> rep = new ReplaceObjectAttributes<>(existingItem);
            rep.replaceWith(item.toEntity());

            Interest savedItem = repository.save(existingItem);
            InterestResponseDTO dto = new InterestResponseDTO(savedItem);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return er.error("Interesse não encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessException(Exception ex, WebRequest request) {
        return er.error("Acesso negado. Nivel de permissão insuficiente", HttpStatus.FORBIDDEN);
    }

}
