package ua.ohonovskiy.onlineshop.product.service.product;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.ohonovskiy.onlineshop.product.DTO.ProductDTO;
import ua.ohonovskiy.onlineshop.product.model.category.Category;
import ua.ohonovskiy.onlineshop.product.model.product.Product;
import ua.ohonovskiy.onlineshop.product.repository.product.ProductRepository;
import ua.ohonovskiy.onlineshop.product.service.category.CategoryService;
import ua.ohonovskiy.onlineshop.product.service.productImage.ProductImageService;
import ua.ohonovskiy.onlineshop.shop.model.Shop;
import ua.ohonovskiy.onlineshop.user.model.User;
import ua.ohonovskiy.onlineshop.user.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImageService productImageService;
    private final CategoryService categoryService;
    private final UserService userService;

    public void save(Product product) {
        productRepository.save(product);
    }

    public Optional<Product> getById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> getAllForShop(Long shopId) {
        return productRepository.findAllByShop_Id(shopId);
    }

    @Transactional
    public void createForCurrentUser(ProductDTO dto) {
        User user = userService.getByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Shop shop = user.getShop();
        if (shop == null) {
            throw new RuntimeException("User has no associated shop");
        }

        Product product = productDTOtoProduct(dto, shop);

        productRepository.save(product);

        product.setProductImages(productImageService.addImage(product.getId(), dto.getImageFiles()));

    }

    public Product productDTOtoProduct(ProductDTO dto, Shop shop) {
        Category category = categoryService.getById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .shop(shop)
                .category(category)
                .build();
    }
}
