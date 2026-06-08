package com.electronic.kafedra.service;

import com.electronic.kafedra.bll.dto.AuthDto;
import com.electronic.kafedra.bll.dto.TokenDto;
import com.electronic.kafedra.bll.service.impl.AuthServiceImpl;
import com.electronic.kafedra.common.exception.AlreadyExistsException;
import com.electronic.kafedra.common.exception.EntityNotFoundException;
import com.electronic.kafedra.dal.entity.Role;
import com.electronic.kafedra.dal.entity.User;
import com.electronic.kafedra.dal.repository.UserRepository;
import com.electronic.kafedra.dal.uow.UnitOfWork;
import com.electronic.kafedra.pl.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UnitOfWork uow;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        when(uow.users()).thenReturn(userRepository);
    }

    @Test
    void login_whenValidCredentials_shouldReturnToken() {
        AuthDto dto = new AuthDto("admin", "password", null);

        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("encodedPassword");
        user.setRole(Role.ADMIN);

        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword"))
                .thenReturn(true);
        when(jwtService.generateToken(user))
                .thenReturn("jwt.token.here");

        TokenDto result = authService.login(dto);

        assertNotNull(result);
        assertEquals("jwt.token.here", result.getToken());
        assertEquals("admin", result.getUsername());
        assertEquals(Role.ADMIN, result.getRole());
    }

    @Test
    void login_whenUserNotFound_shouldThrowEntityNotFoundException() {
        AuthDto dto = new AuthDto("unknown", "password", null);

        when(userRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> authService.login(dto)
        );
    }

    @Test
    void login_whenWrongPassword_shouldThrowAccessDeniedException() {
        AuthDto dto = new AuthDto("admin", "wrongPassword", null);

        User user = new User();
        user.setUsername("admin");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword"))
                .thenReturn(false);

        assertThrows(
                com.electronic.kafedra.common.exception.AccessDeniedException.class,
                () -> authService.login(dto)
        );
    }

    @Test
    void register_whenUsernameUnique_shouldCreateUser() {
        AuthDto dto = new AuthDto("newuser", "password123", Role.STUDENT);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("newuser");
        savedUser.setRole(Role.STUDENT);

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateToken(savedUser)).thenReturn("jwt.token.here");

        TokenDto result = authService.register(dto);

        assertNotNull(result);
        assertEquals("newuser", result.getUsername());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_whenUsernameExists_shouldThrowAlreadyExistsException() {
        AuthDto dto = new AuthDto("existing", "password", Role.STUDENT);

        when(userRepository.existsByUsername("existing")).thenReturn(true);

        assertThrows(AlreadyExistsException.class,
                () -> authService.register(dto)
        );
        verify(userRepository, never()).save(any());
    }
}
