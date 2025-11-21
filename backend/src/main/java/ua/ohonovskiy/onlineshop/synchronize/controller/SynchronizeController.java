package ua.ohonovskiy.onlineshop.synchronize.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.ohonovskiy.onlineshop.synchronize.service.SynchronizeService;

@RestController
@RequestMapping("/synchronize")
@RequiredArgsConstructor
public class SynchronizeController {

    private final SynchronizeService synchronizeService;

    @PostMapping("/import-file")
    public ResponseEntity<Void> postImportFile(@RequestBody String xmlURL) {
        synchronizeService.setImportFileForCurrentUser(xmlURL);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping("/import")
    public ResponseEntity<Void> makeImport() {
        synchronizeService.makeImportForCurrentUserAsync();

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
