package com.org.flyawaytravelapi.auth.domain;

import com.org.flyawaytravelapi.auth.components.JwtService;
import com.org.flyawaytravelapi.auth.dto.request.LoginDTO;
import com.org.flyawaytravelapi.user.application.UserService;
import com.org.flyawaytravelapi.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public String authenticate(LoginDTO loginDTO) {
        // Validar campos obligatorios
        if (loginDTO.getEmail() == null || loginDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (loginDTO.getPassword() == null || loginDTO.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        User user = userService.findByEmail(loginDTO.getEmail());

        // Validar email desconocido
        if (user == null) {
            throw new IllegalArgumentException("Unknown email");
        }

        // Validar contraseña incorrecta
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Wrong password");
        }

        // Generar token JWT
        return jwtService.generateToken(user.getEmail(), user.getId());
    }
}
