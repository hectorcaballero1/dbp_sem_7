package com.org.flyawaytravelapi.user.application;

import com.org.flyawaytravelapi.user.domain.User;
import com.org.flyawaytravelapi.user.dto.request.RegisterUserDTO;
import com.org.flyawaytravelapi.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Z].*");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{8,}$");

    public User registerUser(RegisterUserDTO userDTO) {
        // Validar campos obligatorios
        if (userDTO.getFirstName() == null || userDTO.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (userDTO.getLastName() == null || userDTO.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (userDTO.getEmail() == null || userDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        // Validar formato de email
        if (!EMAIL_PATTERN.matcher(userDTO.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // Validar que el email no esté registrado
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Validar First Name (al menos 1 carácter mayúscula A-Z al inicio)
        if (!NAME_PATTERN.matcher(userDTO.getFirstName()).matches()) {
            throw new IllegalArgumentException("First name must start with an uppercase letter (A-Z)");
        }

        // Validar Last Name (al menos 1 carácter mayúscula A-Z al inicio)
        if (!NAME_PATTERN.matcher(userDTO.getLastName()).matches()) {
            throw new IllegalArgumentException("Last name must start with an uppercase letter (A-Z)");
        }

        // Validar Password (al menos 8 caracteres, con al menos una letra y un número)
        if (!PASSWORD_PATTERN.matcher(userDTO.getPassword()).matches()) {
            throw new IllegalArgumentException("Password must be at least 8 characters long with at least one letter and one number");
        }

        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }
}
