package ua.ohonovskiy.onlineshop.product.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.ohonovskiy.onlineshop.product.model.category.Category;
import ua.ohonovskiy.onlineshop.product.repository.category.CategoryRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository repository;

    public Optional<Category> getById(Long id) {
        return repository.findById(id);
    }
}
