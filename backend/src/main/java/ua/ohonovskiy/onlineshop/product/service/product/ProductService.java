package ua.ohonovskiy.onlineshop.product.service.product;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ua.ohonovskiy.onlineshop.product.DTO.ProductDTO;
import ua.ohonovskiy.onlineshop.product.model.category.Category;
import ua.ohonovskiy.onlineshop.product.model.product.Product;
import ua.ohonovskiy.onlineshop.product.repository.product.ProductRepository;
import ua.ohonovskiy.onlineshop.product.service.category.CategoryService;
import ua.ohonovskiy.onlineshop.product.service.productImage.ProductImageService;
import ua.ohonovskiy.onlineshop.shop.model.Shop;
import ua.ohonovskiy.onlineshop.user.service.UserService;
import ua.ohonovskiy.onlineshop.util.model.SimpleEntityModel;

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

    public List<Product> getAllForShop(Long shopId) {
        return productRepository.findAllByShop_Id(shopId);
    }

    /**
     *  Delete request
     */
    @Transactional
    public void deleteByIdForCurrentUser(Long id) {
        Long shopId = userService.getCurrentUser().getShop().getId();

        if (!productRepository.existsByIdAndShopId(id, shopId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot delete this product");
        }

        deleteById(id);
    }
    private void deleteById(Long id) {
        productImageService.deleteAllByProduct(id);
        productRepository.deleteById(id);
    }


    /**
     *  Create request
     */
    @Transactional
    public void createForCurrentUser(ProductDTO dto) {
        Shop shop = userService.getCurrentUser().getShop();
        if (shop == null) {
            throw new RuntimeException("User has no associated shop");
        }
        Product product = productDTOtoProduct(dto, shop);
        productRepository.save(product);
        product.setProductImages(productImageService.addImage(product.getId(), dto.getImageFiles()));

    }

    /**
     *  Update request
     */
    public void updateForCurrentUser(Long productId, ProductDTO productDTO) {
        Long shopId = userService.getCurrentUser().getShop().getId();

        if (!productRepository.existsByIdAndShopId(productId, shopId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot delete this product");
        }

        update(productId, productDTO);
    }

    private void update(Long productId, ProductDTO productDTO) {
        Product product = productDTOtoProduct(productDTO, userService.getCurrentUser().getShop());
        product.setId(productId);

        save(product);
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
