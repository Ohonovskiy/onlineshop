package ua.ohonovskiy.onlineshop.auth.DTO;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
