package io.github.wilsoncastro.tucrevision.core.service;

import io.github.wilsoncastro.tucrevision.core.model.User;
import io.github.wilsoncastro.tucrevision.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void testExistsByEmailEqualsIgnoreCase() {
        String email = "test@example.com";
        when(userRepository.existsByEmailEqualsIgnoreCase(email)).thenReturn(true);

        assertTrue(userService.existsByEmailEqualsIgnoreCase(email));
        verify(userRepository, times(1)).existsByEmailEqualsIgnoreCase(email);
    }

    @Test
    public void testExistsByEmailEqualsIgnoreCaseAndId() {
        String email = "test@example.com";
        when(userRepository.existsByEmailEqualsIgnoreCaseAndIdIsNot(email, 1L)).thenReturn(true);

        assertTrue(userRepository.existsByEmailEqualsIgnoreCaseAndIdIsNot(email, 1L));
        verify(userRepository, times(1)).existsByEmailEqualsIgnoreCaseAndIdIsNot(email, 1L);
    }

    @Test
    public void testCreateUser() {
        String email = "test@example.com";
        String password = "password";
        String name = "Test User";

        when(userRepository.existsByEmailEqualsIgnoreCase(email)).thenReturn(false);
        when(encoder.encode(anyString())).thenReturn("criptado");
        when(userRepository.save(any(User.class))).thenReturn(new User(name, email, "criptado", true));

        User user = userService.createUser(name, email, password);

        assertNotNull(user);
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals("criptado", user.getPassword());
        assertTrue(user.isActive());
        verify(userRepository, times(1)).existsByEmailEqualsIgnoreCase(email);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdateUser() {
        User user = new User("Test user","test@example.com", "password", true);
        when(userRepository.save(user)).thenReturn(user);

        User updatedUser = userService.updateUser(user);

        assertNotNull(updatedUser);
        assertEquals(user.getEmail(), updatedUser.getEmail());
        assertEquals(user.getPassword(), updatedUser.getPassword());
        assertEquals(user.isActive(), updatedUser.isActive());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testDeleteUser() {
        User user = new User("Test User", "test@example.com", "password", true);

        userService.deleteUser(user);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void testFindAll() {
        int page = 0;
        int size = 10;
        Page<User> expectedPage = new PageImpl<>(Collections.emptyList());
        when(userRepository.findAll(PageRequest.of(page, size))).thenReturn(expectedPage);

        Page<User> resultPage = userService.findAll(page, size);

        assertNotNull(resultPage);
        assertEquals(expectedPage, resultPage);
        verify(userRepository, times(1)).findAll(PageRequest.of(page, size));
    }

    @Test
    public void testFindByID() {
        Long id = 1L;
        User user = new User("Test user", "test@example.com", "password", true);
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        assertEquals(user, userService.findByID(id));

        verify(userRepository, times(1)).findById(id);
    }
}
