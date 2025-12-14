package ua.ohonovskiy.onlineshop.synchronize.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;
import ua.ohonovskiy.onlineshop.shop.model.Shop;
import ua.ohonovskiy.onlineshop.shop.service.ShopService;
import ua.ohonovskiy.onlineshop.synchronize.enums.ImportStatus;
import ua.ohonovskiy.onlineshop.synchronize.model.ImportSubmit;
import ua.ohonovskiy.onlineshop.synchronize.repository.ImportSubmitRepository;
import ua.ohonovskiy.onlineshop.user.model.User;
import ua.ohonovskiy.onlineshop.user.service.UserService;
import ua.ohonovskiy.onlineshop.util.exception.ImportNotExistsException;
import ua.ohonovskiy.onlineshop.util.exception.ShopNotExistsException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImportSubmitService {

    private final ImportSubmitRepository repo;
    private final UserService userService;
    private final ShopService shopService;

    public void save(ImportSubmit importSubmit) {
        repo.save(importSubmit);
    }

    @Transactional(readOnly = true)
    public ImportSubmit getByIdWithShop(Long id) {
        return repo.findByIdWithShop(id)
                .orElseThrow(() -> new RuntimeException("ImportSubmit not found: " + id));
    }

    public List<ImportSubmit> getAllForCurrentUser() {
        return userService.getCurrentUser().getShop().getImportSubmits();
    }

    public Optional<ImportSubmit> getEldestAccepted() {
        return repo.findEldestAccepted();
    }

    public ImportSubmit getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("ImportSubmit not found: " + id));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean claimProcessing(Long id) {
        int updated = repo.claim(id, ImportStatus.STATUS_ACCEPTED, ImportStatus.STATUS_PROCESSING);
        return updated == 1;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markDone(Long id) {
        ImportSubmit s = getById(id);
        s.setImportStatus(ImportStatus.STATUS_DONE);
        s.setFinishedAt(Instant.now());
        s.setErrorMessage(null);
        repo.save(s);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markFailed(Long id, String reason) {
        ImportSubmit s = getById(id);
        s.setImportStatus(ImportStatus.STATUS_FAILED);
        s.setFinishedAt(Instant.now());
        s.setErrorMessage(reason);
        repo.save(s);
    }

    public void makeImportForCurrentUser() {
        User user = userService.getCurrentUser();

        if(user.getShop() == null)
            throw new ShopNotExistsException("User doesn't own any shop");

        Shop shop = user.getShop();

        if(shop.getImportFileURL().isBlank())
            throw new ImportNotExistsException("Import file URL is empty");

        if (shop.getImportSubmits()
                .stream()
                .anyMatch(i ->
                        (i.getImportStatus().equals(ImportStatus.STATUS_ACCEPTED)
                                || i.getImportStatus().equals(ImportStatus.STATUS_PROCESSING))
                )) {
            return;
        }

        ImportSubmit is = ImportSubmit.builder()
                .shop(shop)
                .importStatus(ImportStatus.STATUS_ACCEPTED)
                .build();
        shop.getImportSubmits().add(is);

        save(is);

        shopService.save(shop);
    }

    public void setImportFileForCurrentUser(String xmlURL) {
        User user = userService.getCurrentUser();

        if(user.getShop() == null)
            throw new ShopNotExistsException("User doesn't own any shop");

        Shop shop = user.getShop();
        shop.setImportFileURL(xmlURL);

        shopService.save(shop);
    }
}
