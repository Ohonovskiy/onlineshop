package ua.ohonovskiy.onlineshop.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.ohonovskiy.onlineshop.shop.DTO.ShopDTO;
import ua.ohonovskiy.onlineshop.shop.service.ShopService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shop")
public class ShopController {
    private final ShopService shopService;

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody ShopDTO shopDTO) {
        shopService.createForCurrentUser(shopDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
