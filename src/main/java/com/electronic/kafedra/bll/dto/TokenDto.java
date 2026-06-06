package com.electronic.kafedra.bll.dto;

import com.electronic.kafedra.dal.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    private String token;
    private String username;
    private Role role;
}
