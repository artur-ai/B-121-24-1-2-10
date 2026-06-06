package com.electronic.kafedra.bll.service;

import com.electronic.kafedra.bll.dto.AuthDto;
import com.electronic.kafedra.bll.dto.TokenDto;

public interface AuthService {
    TokenDto login(AuthDto dto);

    TokenDto register(AuthDto dto);
}
