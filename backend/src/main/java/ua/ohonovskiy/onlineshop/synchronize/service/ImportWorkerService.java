package ua.ohonovskiy.onlineshop.synchronize.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImportWorkerService {

    private final ImportProcessorService processor;

    @Async("importExecutor")
    public void run(Long importSubmitId) {
        processor.importProducts(importSubmitId);
    }
}

