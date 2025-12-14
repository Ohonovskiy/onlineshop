package ua.ohonovskiy.onlineshop.synchronize.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SynchronizeService {

    private final ImportSubmitService importSubmitService;
    private final ImportWorkerService importWorkerService;

    @Scheduled(fixedDelay = 3000)
    public void checkImportQueue() {
        var nextOpt = importSubmitService.getEldestAccepted();
        if (nextOpt.isEmpty()) return;

        Long taskId = nextOpt.get().getId();

        boolean claimed = importSubmitService.claimProcessing(taskId);
        if (!claimed) return;

        importWorkerService.run(taskId);
    }
}
