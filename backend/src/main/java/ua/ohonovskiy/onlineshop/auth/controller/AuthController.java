package ua.ohonovskiy.onlineshop.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.ohonovskiy.onlineshop.auth.DTO.AuthRequest;
import ua.ohonovskiy.onlineshop.auth.DTO.AuthResponse;
import ua.ohonovskiy.onlineshop.auth.DTO.RegisterRequest;
import ua.ohonovskiy.onlineshop.security.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Backend is alive ✅");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        String token = authService.login(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        authService.register(request);

        return ResponseEntity.ok("User registered successfully");
    }
}
