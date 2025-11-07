package ua.ohonovskiy.onlineshop.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.ohonovskiy.onlineshop.product.DTO.ProductDTO;
import ua.ohonovskiy.onlineshop.product.service.product.ProductService;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public String index() {
        return productService.getAll().toString();
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
        productService.update(productId, productDTO);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
