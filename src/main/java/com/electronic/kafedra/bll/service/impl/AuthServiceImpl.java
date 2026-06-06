package com.electronic.kafedra.bll.service.impl;

import com.electronic.kafedra.bll.dto.AuthDto;
import com.electronic.kafedra.bll.dto.TokenDto;
import com.electronic.kafedra.bll.service.AuthService;
import com.electronic.kafedra.common.exception.AlreadyExistsException;
import com.electronic.kafedra.common.exception.EntityNotFoundException;
import com.electronic.kafedra.dal.entity.Role;
import com.electronic.kafedra.dal.entity.User;
import com.electronic.kafedra.dal.uow.UnitOfWork;
import com.electronic.kafedra.pl.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UnitOfWork uow;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UnitOfWork uow, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.uow = uow;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public TokenDto login(AuthDto dto) {
        User user = uow.users().findByUsername(dto.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User not found: " + dto.getUsername()
                ));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new com.electronic.kafedra.common.exception
                    .AccessDeniedException("Invalid password");
        }

        String token = jwtService.generateToken(user);
        return new TokenDto(token, user.getUsername(), user.getRole());
    }

    @Override
    public TokenDto register(AuthDto dto) {
        if (uow.users().existsByUsername(dto.getUsername())) {
            throw new AlreadyExistsException(
                    "Username already exists: " + dto.getUsername()
            );
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole() != null ? dto.getRole() : Role.GUEST);

        User saved = uow.users().save(user);
        String token = jwtService.generateToken(saved);
        return new TokenDto(token, saved.getUsername(), saved.getRole());
    }
}
