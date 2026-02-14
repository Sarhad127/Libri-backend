package org.tutorial.venusbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tutorial.venusbackend.exceptions.UnauthorizedException;
import org.tutorial.venusbackend.model.MyUser;
import org.tutorial.venusbackend.repository.MyUserRepository;

@Service
@RequiredArgsConstructor
public class AuthHelperService {

    private final JwtService jwtService;
    private final MyUserRepository userRepository;

    public MyUser getUserFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;

        String token = authHeader.substring(7);
        String email;

        try {
            email = jwtService.extractUsername(token);
        } catch (Exception e) {
            System.out.println("JWT parse error: " + e.getMessage());
            return null;
        }

        return userRepository.findByEmail(email).orElse(null);
    }

    public MyUser authenticateUser(String authHeader) {
        MyUser user = getUserFromToken(authHeader);
        if (user == null) {
            throw new UnauthorizedException();
        }
        return user;
    }
}