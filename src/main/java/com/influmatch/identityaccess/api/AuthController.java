// src/main/java/com/influmatch/identityaccess/api/AuthController.java
package com.influmatch.identityaccess.api;

import com.influmatch.identityaccess.application.AuthService;
import com.influmatch.identityaccess.domain.model.RoleEnum;
import com.influmatch.identityaccess.domain.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService auth;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest req) {
        User registeredUser = auth.register(
                req.email(),
                req.password(),
                RoleEnum.valueOf(req.role().name())
        );

        String token = auth.generateToken(registeredUser);

        var body = new RegisterResponse(
            registeredUser.getId(),
            registeredUser.getEmail(),
            registeredUser.getRole().name(),
            token,
            "Registro exitoso!"
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
}
