package ua.ohonovskiy.onlineshop.synchronize.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ua.ohonovskiy.onlineshop.shop.model.Shop;
import ua.ohonovskiy.onlineshop.shop.service.ShopService;
import ua.ohonovskiy.onlineshop.user.model.User;
import ua.ohonovskiy.onlineshop.user.service.UserService;

@Service
@RequiredArgsConstructor
public class SynchronizeService {

    private final UserService userService;
    private final ShopService shopService;

    public void setImportFileForCurrentUser(String xmlURL) {
        User user = userService.getCurrentUser();
        Shop shop = user.getShop();

        if (shop == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User has no associated shop");
        }

        shop.setImportFileURL(xmlURL);
        shopService.save(shop);
    }

    public void synchronize() {

    }

    public void importProducts(User user) {
        Shop shop = user.getShop();

        if (shop == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User has no associated shop");
        }

        if(shop.getImportFileURL() == null) {
            throw new ResponseStatusException
                    (HttpStatus.BAD_REQUEST, "User's shop has no associated import file URL");
        }

        String importFileUrl = shop.getImportFileURL();


    }
}
