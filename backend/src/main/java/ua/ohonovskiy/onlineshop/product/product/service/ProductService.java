package ua.ohonovskiy.onlineshop.product.product.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ua.ohonovskiy.onlineshop.product.category.model.Category;
import ua.ohonovskiy.onlineshop.product.category.service.CategoryService;
import ua.ohonovskiy.onlineshop.product.product.DTO.ProductDTO;
import ua.ohonovskiy.onlineshop.product.product.model.Product;
import ua.ohonovskiy.onlineshop.product.product.repository.ProductRepository;
import ua.ohonovskiy.onlineshop.product.productImage.service.ProductImageService;
import ua.ohonovskiy.onlineshop.product.productView.service.ProductViewService;
import ua.ohonovskiy.onlineshop.shop.model.Shop;
import ua.ohonovskiy.onlineshop.shop.service.ShopService;
import ua.ohonovskiy.onlineshop.user.service.UserService;

import java.time.LocalDateTime;
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
    private final ProductViewService productViewService;

    public Product save(Product product) {
       return productRepository.save(product);
    }

    public Optional<Product> getById(String id) {
        return productRepository.findById(id);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public List<Product> getAllById(List<String> ids) {
        return productRepository.findAllById(ids);
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
    //  Add more clever formula
    public List<ProductDTO> getPopular(Integer productsCount) {
        // popular = views/week

        return productToDTO(                            // casting to DTO
                getAllById(productViewService           // getting models
                        .getTopMostViewedFrom(          // getting ids for most popular products
                                productsCount, LocalDateTime.now().minusDays(7)
                        )
                )
        );

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
    public void deleteByIdForCurrentUser(String id) {

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

    public void deleteById(String id) {
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
    public void updateForCurrentUser(String productId, ProductDTO productDTO) {
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

    public void update(String productId, ProductDTO productDTO) {
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
