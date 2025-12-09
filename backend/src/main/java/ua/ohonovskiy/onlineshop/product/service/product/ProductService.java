package ua.ohonovskiy.onlineshop.product.service.product;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
import ua.ohonovskiy.onlineshop.shop.service.ShopService;
import ua.ohonovskiy.onlineshop.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImageService productImageService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ShopService shopService;

    public Product save(Product product) {
       return productRepository.save(product);
    }

    public Optional<Product> getById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public List<Product> getAllForShop(Long shopId) {
        return shopService.getById(shopId)
                .orElseThrow()
                .getProducts();
    }

    public List<ProductDTO> getLatest(Integer productsCount) {
        return productToDTO(productRepository.findAllByOrderByCreatedOnDesc(
                PageRequest.of(0, productsCount)
        ));
    }

    // TODO
    //  Add formula after [Product views, Orders, ...] implemented
    public List<ProductDTO> getPopular(Integer productsCount) {
        return null;
    }

    public List<ProductDTO> getTopDiscounted(Integer productsCount) {
        return productToDTO(productRepository.findTopDiscounted(
                PageRequest.of(0, productsCount)
        ));
    }

    /**
     *  Delete request
     */
    @Transactional
    public void deleteByIdForCurrentUser(Long id) {

        // check if current user owes the product
        if (userService
                .getCurrentUser()
                .getShop()
                .getProducts()
                .stream()
                .noneMatch(p -> p.getId().equals(id)
                )
        ) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot delete this product");
        }

        deleteById(id);
    }

    public void deleteById(Long id) {
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
        Product product = productDTOtoProduct(dto);
        productRepository.save(product);
        product.setProductImages(productImageService.addImage(product.getId(), dto.getImageFiles()));

    }

    /**
     *  Update request
     */
    public void updateForCurrentUser(Long productId, ProductDTO productDTO) {
        if (userService
                .getCurrentUser()
                .getShop()
                .getProducts()
                .stream()
                .noneMatch(p -> p.getId().equals(productId)
                )
        ) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot delete this product");
        }

        update(productId, productDTO);
    }

    public void update(Long productId, ProductDTO productDTO) {
        Product product = productDTOtoProduct(productDTO);
        product.setId(productId);

        save(product);
    }



    public List<ProductDTO> productToDTO(List<Product> products) {
        List<ProductDTO> dtoList = new ArrayList<>();

        for(Product p : products) {
            dtoList.add(productToDTO(p));
        }

        return dtoList;
    }

    public ProductDTO productToDTO(Product product) {
        return ProductDTO.builder()
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .categoryId(product.getCategory().getId())
                .imgURL(product
                        .getProductImages()
                        .get(0)
                        .getImageUrl()
                )
                .build();
    }

    public Product productDTOtoProduct(ProductDTO dto) {
        Category category = categoryService.getById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .category(category)
                .build();
    }

    public List<ProductDTO> getAllDTO() {
        List<ProductDTO> dtoList = new ArrayList<>();

        for(Product p : productRepository.findAll()) {
            dtoList.add(productToDTO(p));
        }

        return dtoList;
    }
}
