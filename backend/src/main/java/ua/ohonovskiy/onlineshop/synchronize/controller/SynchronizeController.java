package ua.ohonovskiy.onlineshop.synchronize.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.ohonovskiy.onlineshop.synchronize.DTO.ImportFileDTO;
import ua.ohonovskiy.onlineshop.synchronize.model.ImportSubmit;
import ua.ohonovskiy.onlineshop.synchronize.service.ImportSubmitService;

import java.util.List;

@RestController
@RequestMapping("/synchronize")
@RequiredArgsConstructor
public class SynchronizeController {
    private final ImportSubmitService importSubmitService;

    @PostMapping("/import-file")
    public ResponseEntity<Void> postImportFile(@RequestBody ImportFileDTO request) {
        importSubmitService.setImportFileForCurrentUser(request);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping("/import")
    public ResponseEntity<Void> makeImport() {
        importSubmitService.makeImportForCurrentUser();

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/import")
    public ResponseEntity<List<ImportSubmit>> getImportInfo() {
        return ResponseEntity.ok(importSubmitService.getAllForCurrentUser());
    }
}
