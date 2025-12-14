package ua.ohonovskiy.onlineshop.product.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.ohonovskiy.onlineshop.product.category.model.Category;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    List<Category> findAllByNameIn(Collection<String> names);
}
