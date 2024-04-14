package io.github.wilsoncastro.tucrevision.core.controller;

import io.github.wilsoncastro.tucrevision.core.pojo.CreateUserDTO;
import io.github.wilsoncastro.tucrevision.core.pojo.UpdateUserDTO;
import io.github.wilsoncastro.tucrevision.core.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    private final UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
            .scheme("http").host("api/users");

    @PostMapping
    @Transactional
    public ResponseEntity<?> createUser(@RequestBody CreateUserDTO createUserPayload) {
        log.info("Creating user with email: {}", createUserPayload.email());

        var created = userService.createUser(createUserPayload.name(), createUserPayload.email().toLowerCase(),
                createUserPayload.password());

        log.info("User created with email: {}", created.getEmail());

        URI uri = uriBuilder.path("/{id}").buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        log.info("Getting user with id: {}", id);

        var user = userService.findByID(id);

        log.info("User found with email: {}", user.getEmail());

        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UpdateUserDTO updateUserPayload) {
        log.info("Updating user with id: {}", id);

        var user = userService.findByID(id);
        user.setName(updateUserPayload.name());
        user.setEmail(updateUserPayload.email().toLowerCase());
        user.setPassword(updateUserPayload.password());

        var updated = userService.updateUser(user);

        log.info("User updated with email: {}", updated.getEmail());

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with id: {}", id);

        var user = userService.findByID(id);

        userService.deleteUser(user);

        log.info("User deleted with email: {}", user.getEmail());

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        log.info("Getting all users");

        var users = userService.findAll(page, size);

        log.info("Users found: {}", users.getTotalElements());

        return ResponseEntity.ok(users);
    }

    @GetMapping("/exists/{email}")
    public ResponseEntity<?> existsByEmail(@PathVariable String email, @RequestParam(required = false) Long id){
        log.info("Checking if user exists with email: {}", email);

        boolean exists;
        if (id == null) {
            exists = userService.existsByEmailEqualsIgnoreCase(email);
        } else {
            exists = userService.existsByEmailEqualsIgnoreCaseAndId(email, id);
        }

        log.info("User exists: {}", exists);

        return ResponseEntity.ok(exists);
    }


}
