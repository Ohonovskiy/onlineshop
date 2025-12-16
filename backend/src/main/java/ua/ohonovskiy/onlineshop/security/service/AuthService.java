package ua.ohonovskiy.onlineshop.security.service;

import lombok.RequiredArgsConstructor;
import ua.ohonovskiy.onlineshop.auth.DTO.RegisterRequest;
import ua.ohonovskiy.onlineshop.security.jwt.JwtService;
import ua.ohonovskiy.onlineshop.user.enums.Role;
import ua.ohonovskiy.onlineshop.user.model.User;
import ua.ohonovskiy.onlineshop.user.service.CustomUserDetailsService;
import ua.ohonovskiy.onlineshop.user.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public String login(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        UserDetails user = userDetailsService.loadUserByUsername(email);

        return jwtService.generateToken(user);
    }

    public void register(RegisterRequest request) {
        if (userService.getByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        userService.save(
                User.builder()
                        .name(request.getName())
                        .surname(request.getSurname())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .enabled(true)
                        .role(Role.ROLE_USER)
                .build()
        );
    }
}

