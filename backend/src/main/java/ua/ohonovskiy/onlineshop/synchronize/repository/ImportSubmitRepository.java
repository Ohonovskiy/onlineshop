package ua.ohonovskiy.onlineshop.synchronize.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.ohonovskiy.onlineshop.synchronize.enums.ImportStatus;
import ua.ohonovskiy.onlineshop.synchronize.model.ImportSubmit;

import java.util.Optional;

@Repository
public interface ImportSubmitRepository extends JpaRepository<ImportSubmit, Long> {
    Optional<ImportSubmit> findFirstByImportStatusOrderByCreatedOnAsc(ImportStatus importStatus);
}
