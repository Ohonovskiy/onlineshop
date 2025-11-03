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

        User user = new User();
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        user.setRole(Role.ROLE_USER);

        userService.save(user);
    }
}

