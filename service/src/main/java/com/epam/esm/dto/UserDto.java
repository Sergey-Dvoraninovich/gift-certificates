package com.epam.esm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserDto {
    private long id;
    private String login;
    private String name;
    private String surname;
    private String email;
}
