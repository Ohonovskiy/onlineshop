package ua.ohonovskiy.onlineshop.synchronize.DTO;

import lombok.Builder;
import lombok.Data;
import ua.ohonovskiy.onlineshop.synchronize.enums.ImportStatus;

@Data
@Builder
public class ImportSubmitStatusResponse {
    private ImportStatus importStatus;
}
