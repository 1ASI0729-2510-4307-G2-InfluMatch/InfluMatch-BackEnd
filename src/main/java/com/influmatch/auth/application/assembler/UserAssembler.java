package com.influmatch.auth.application.assembler;

import com.influmatch.auth.application.dto.RegisterRequest;
import com.influmatch.auth.domain.model.User;
import com.influmatch.auth.domain.model.valueobject.Email;
import com.influmatch.auth.domain.model.valueobject.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// Definición del componente UserAssembler, utilizado para ensamblar datos relacionados con el usuario
@Component
@RequiredArgsConstructor  // Genera un constructor con parámetros para las dependencias finales (como passwordEncoder)
public class UserAssembler {
    
    // Dependencia final para codificar contraseñas
    private final PasswordEncoder passwordEncoder;

    // Método para convertir un objeto RegisterRequest en un objeto User (entidad)
    public User toEntity(RegisterRequest request) {
        // Se crea una nueva instancia de User con los datos proporcionados en el request
        return new User(
            new Email(request.getEmail()), // Crea un nuevo objeto Email a partir del correo del request
            new Password(passwordEncoder.encode(request.getPassword())), // Codifica la contraseña antes de asignarla
            request.getRole() // Asigna el rol proporcionado en el request
        );
    }
}
