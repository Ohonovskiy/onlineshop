package ua.ohonovskiy.onlineshop.user.controller;

import lombok.RequiredArgsConstructor;
import ua.ohonovskiy.onlineshop.user.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
}
