package ua.ohonovskiy.onlineshop.product.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.ohonovskiy.onlineshop.product.category.model.Category;
import ua.ohonovskiy.onlineshop.product.category.repository.CategoryRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Optional<Category> getByName(String name) {
        return categoryRepository.findByName(name);
    }

    public Optional<Category> getById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public List<Category> saveAll(List<Category> categories) {
        return categoryRepository.saveAll(categories);
    }

    @Transactional
    public void flush() {
        categoryRepository.flush();
    }

    /**
     * Витягує існуючі категорії одним запитом IN (...)
     * і створює відсутні через saveAll().
     * Повертає map: name -> Category
     */
    @Transactional
    public Map<String, Category> getOrCreateByNames(Collection<String> names) {
        if (names == null || names.isEmpty()) return new HashMap<>();

        List<Category> existing = categoryRepository.findAllByNameIn(names);

        Map<String, Category> byName = new HashMap<>();
        for (Category c : existing) {
            byName.put(c.getName(), c);
        }

        List<Category> toCreate = new ArrayList<>();
        for (String name : names) {
            if (name == null) continue;
            String trimmed = name.trim();
            if (trimmed.isEmpty()) continue;

            if (!byName.containsKey(trimmed)) {
                Category c = new Category(trimmed, null);
                toCreate.add(c);
                byName.put(trimmed, c);
            }
        }

        if (!toCreate.isEmpty()) {
            categoryRepository.saveAll(toCreate);
            categoryRepository.flush();
        }

        return byName;
    }
}
