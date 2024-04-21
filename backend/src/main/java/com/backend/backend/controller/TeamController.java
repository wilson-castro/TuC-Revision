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
import com.backend.backend.models.team.member.Member;
import com.backend.backend.models.team.member.MemberRepo;
import com.backend.backend.models.team.member.MemberRequestDTO;
import com.backend.backend.models.team.member.MemberResponseDTO;
import com.backend.backend.models.users.User;
import com.backend.backend.models.users.UserRepo;
import com.backend.backend.utils.ReplaceObjectAttributes;
import com.backend.backend.utils.ReponseBuilder;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/teams")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TeamController {

    @Autowired
    private TeamRepo repository;
    @Autowired
    private MemberRepo memberRepo;

    @Autowired
    private UserRepo userRepo;

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

        Page<TeamResponseDTO> findAll = repository.findAll(of).map(Team::toDTO);
        return findAll;
    }

    @PostMapping
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

    @GetMapping("/{teamId}/members/{id}")
    public ResponseEntity<?> getMemberById(@PathVariable Long teamId, @PathVariable Long id) {
        Optional<Member> existingItemOptional = memberRepo.getTeamMember(teamId, id);

        if (existingItemOptional.isPresent()) {
            Member existingItem = existingItemOptional.get();

            return new ResponseEntity<MemberResponseDTO>(existingItem.toDTO(), HttpStatus.OK);
        } else {

            return er.error("Membro não encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{teamId}/members")
    public Page<MemberResponseDTO> getMembersOf(@PathVariable Long teamId, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest of = PageRequest.of(page, size);

        Page<MemberResponseDTO> findAll = memberRepo.getAllMembers(of, teamId).map((member) -> member.toDTO());
        return findAll;
    }

    @PostMapping("/{teamId}/members")
    public ResponseEntity<?> createMember(@PathVariable Long teamId, @RequestBody @Valid MemberRequestDTO item) {
        try {
            Member itemToBeSaved = item.toEntity();
            Long userId = item.getUserId();
            Optional<Team> team = repository.findById(teamId);
            Optional<User> user = userRepo.findById(userId);
            if (team.isEmpty()) {
                return er.error("O time informado não existe", HttpStatus.NOT_FOUND);
            }
            if (user.isEmpty()) {
                return er.error("O usuário informado não existe", HttpStatus.NOT_FOUND);
            }
            Optional<Member> teamMember = memberRepo.getTeamMember(teamId, userId);

            if (teamMember.isPresent()) {
                return er.error("O usuário informado já faz parte do time", HttpStatus.BAD_REQUEST);
            }

            itemToBeSaved.setTeam(team.get());
            itemToBeSaved.setUser(user.get());
            Member savedItem = memberRepo.save(itemToBeSaved);
            return new ResponseEntity<>(savedItem.toDTO(), HttpStatus.CREATED);
        } catch (Exception e) {

            return er.error("Erro ao criar membro:" + e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("/{teamId}/members/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Long teamId, @PathVariable Long id) {
        Optional<Member> existingItemOptional = memberRepo.getTeamMember(teamId, id);

        if (existingItemOptional.isPresent()) {
            Long idLong = existingItemOptional.get().getId();
            memberRepo.deleteById(idLong);
            return new ResponseEntity<>(idLong, HttpStatus.OK);
        } else {
            return er.error("Membro não encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<?> handleAccessException(Exception ex, WebRequest request) {
        return er.error("Acesso negado. Nivel de permissão insuficiente", HttpStatus.FORBIDDEN);
    }

}
