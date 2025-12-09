package ua.ohonovskiy.onlineshop.homepage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.ohonovskiy.onlineshop.homepage.DTO.HomePageProductsResponse;
import ua.ohonovskiy.onlineshop.recommendation.service.RecommendationService;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomePageController {
    private final RecommendationService recommendationService;

    @GetMapping
    public ResponseEntity<HomePageProductsResponse> getHomePageProducts() {
        return ResponseEntity.ok(recommendationService.getRecommendationProducts());
    }
}
