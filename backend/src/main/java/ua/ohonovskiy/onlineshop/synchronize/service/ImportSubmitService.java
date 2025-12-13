package ua.ohonovskiy.onlineshop.synchronize.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.ohonovskiy.onlineshop.shop.model.Shop;
import ua.ohonovskiy.onlineshop.shop.service.ShopService;
import ua.ohonovskiy.onlineshop.synchronize.enums.ImportStatus;
import ua.ohonovskiy.onlineshop.synchronize.model.ImportSubmit;
import ua.ohonovskiy.onlineshop.synchronize.repository.ImportSubmitRepository;
import ua.ohonovskiy.onlineshop.user.service.UserService;
import ua.ohonovskiy.onlineshop.util.exception.ImportNotExistsException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImportSubmitService {
    private final ImportSubmitRepository importSubmitRepository;
    private final UserService userService;
    private final ShopService shopService;

    public Optional<ImportSubmit> getEldestAccepted() {
        return importSubmitRepository
                .findFirstByImportStatusOrderByCreatedOnAsc(
                        ImportStatus.STATUS_ACCEPTED
                );
    }

    public ImportSubmit save(ImportSubmit importSubmit) {
        return importSubmitRepository.save(importSubmit);
    }

    public List<ImportSubmit> getImportSubmitsForCurrentUser() {
        return userService.getCurrentUser().getShop().getImportSubmits();
    }

    public void setImportFileForCurrentUser(String xmlURL) {
        Shop shop = userService.getCurrentUser().getShop();
        shop.setImportFileURL(xmlURL);
        shopService.save(shop);
    }

    public void makeImportForCurrentUserAsync() {
        Shop shop = userService.getCurrentUser().getShop();
        String importFileURL = shop.getImportFileURL();

        if (importFileURL.isBlank()) throw new ImportNotExistsException("Import file URL is empty");

        ImportSubmit importSubmit = new ImportSubmit();
        importSubmit.setShop(shop);

        save(importSubmit);

        shop.addImportSubmit(importSubmit);
        shopService.save(shop);
    }
}
