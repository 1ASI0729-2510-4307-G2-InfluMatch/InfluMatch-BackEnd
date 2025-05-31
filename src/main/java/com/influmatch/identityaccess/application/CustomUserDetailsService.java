// src/main/java/com/influmatch/identityaccess/application/CustomUserDetailsService.java
package com.influmatch.identityaccess.application;

import com.influmatch.identityaccess.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        return repo.findByEmail(email)
                   .map(u -> User.withUsername(u.getEmail())
                                 .password(u.getPasswordHash())
                                 .roles(u.getRole().name())
                                 .build())
                   .orElseThrow(() -> new UsernameNotFoundException(email));
    }
}
