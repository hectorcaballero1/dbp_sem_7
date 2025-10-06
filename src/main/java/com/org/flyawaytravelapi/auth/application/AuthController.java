package com.org.flyawaytravelapi.auth.application;

import com.org.flyawaytravelapi.auth.domain.AuthService;
import com.org.flyawaytravelapi.auth.dto.request.LoginDTO;
import com.org.flyawaytravelapi.auth.dto.response.AuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO login) {
        try {
            String token = authService.authenticate(login);
            return ResponseEntity.ok(new AuthToken(token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }
}
