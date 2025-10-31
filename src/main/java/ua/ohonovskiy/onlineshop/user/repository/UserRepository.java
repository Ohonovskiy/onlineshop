package ua.ohonovskiy.onlineshop.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.ohonovskiy.onlineshop.user.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getReferenceByEmail(String email);
}
