package ua.ohonovskiy.onlineshop.synchronize.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
