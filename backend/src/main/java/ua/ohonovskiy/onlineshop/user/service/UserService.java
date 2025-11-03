package ua.ohonovskiy.onlineshop.user.service;

import lombok.RequiredArgsConstructor;
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

    public void save(User user) {
        userRepository.save(user);
    }

    public Optional<User> getByEmail(String email) {
        return userRepository.getReferenceByEmail(email);
    }
}
