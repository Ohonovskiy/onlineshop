package ua.ohonovskiy.onlineshop.product.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.ohonovskiy.onlineshop.product.model.category.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
