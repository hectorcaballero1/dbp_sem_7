package com.org.flyawaytravelapi.user.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
