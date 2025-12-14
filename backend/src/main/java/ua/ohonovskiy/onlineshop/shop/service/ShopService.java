package ua.ohonovskiy.onlineshop.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ua.ohonovskiy.onlineshop.shop.DTO.ShopDTO;
import ua.ohonovskiy.onlineshop.shop.model.Shop;
import ua.ohonovskiy.onlineshop.shop.repository.ShopRepository;
import ua.ohonovskiy.onlineshop.user.model.User;
import ua.ohonovskiy.onlineshop.user.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopService {
    private final ShopRepository shopRepository;
    private final UserService userService;

    @Transactional
    public Shop save(Shop shop) {
        return shopRepository.save(shop);
    }

    public void delete(Shop shop) {
        shopRepository.delete(shop);
    }

    public void deleteById(Long id) {
        shopRepository.deleteById(id);
    }

    public Optional<Shop> getById(Long id) {
        return Optional.of(shopRepository.getReferenceById(id));
    }

    public void createForCurrentUser(ShopDTO shopDTO) {
        User currentUser = userService.getByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Shop newShop = Shop.builder()
                .name(shopDTO.getName())
                .description(shopDTO.getDescription())
                .build();

        shopRepository.save(newShop);

        currentUser.setShop(newShop);

        userService.save(currentUser);
    }
}
