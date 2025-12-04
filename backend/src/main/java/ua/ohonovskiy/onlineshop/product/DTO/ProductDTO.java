package ua.ohonovskiy.onlineshop.product.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private String name;
    private String description;
    private double price;
    private Long categoryId;
    private String imgURL;
    private List<MultipartFile> imageFiles;
}

