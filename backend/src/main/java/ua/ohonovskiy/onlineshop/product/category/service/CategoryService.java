package ua.ohonovskiy.onlineshop.product.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.ohonovskiy.onlineshop.product.category.model.Category;
import ua.ohonovskiy.onlineshop.product.category.repository.CategoryRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository repository;

    public Optional<Category> getById(Long id) {
        return repository.findById(id);
    }

    public Optional<Category> getByName(String name) {
        return repository.findByName(name);
    }

    public Category save(Category category) {
        return repository.save(category);
    }
}
