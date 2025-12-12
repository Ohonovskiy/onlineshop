package ua.ohonovskiy.onlineshop.product.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.ohonovskiy.onlineshop.product.product.DTO.ProductDTO;
import ua.ohonovskiy.onlineshop.product.product.model.Product;
import ua.ohonovskiy.onlineshop.product.product.service.ProductService;
import ua.ohonovskiy.onlineshop.product.productView.service.ProductViewService;

import jakarta.servlet.http.HttpServletRequest;
import ua.ohonovskiy.onlineshop.user.model.CustomUserDetails;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductViewService productViewService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> index() {
        return ResponseEntity.ok(productService.getAllDTO());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return productService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<Void> registerView(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        productViewService.registerView(id, userDetails);

        return ResponseEntity.ok().build();
    }



    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createProduct (
            @RequestPart("product") ProductDTO productDTO,
            @RequestPart(value = "imageFiles") List<MultipartFile> imageFiles) {

        productDTO.setImageFiles(imageFiles);
        productService.createForCurrentUser(productDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteByIdForCurrentUser(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateProduct(
            Long productId,
            @RequestPart("product") ProductDTO productDTO,
            @RequestPart(value = "imageFiles") List<MultipartFile> imageFiles) {

        productDTO.setImageFiles(imageFiles);
        productService.updateForCurrentUser(productId, productDTO);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
