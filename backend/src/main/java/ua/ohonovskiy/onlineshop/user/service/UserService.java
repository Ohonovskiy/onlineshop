package ua.ohonovskiy.onlineshop.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.ohonovskiy.onlineshop.user.model.User;
import ua.ohonovskiy.onlineshop.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.getReferenceById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getByEmail(String email) {
        return userRepository.getReferenceByEmail(email);
    }

    public List<User> getAllShopOwners() {
        return userRepository.getAllByShopNotNull();
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.getReferenceByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
