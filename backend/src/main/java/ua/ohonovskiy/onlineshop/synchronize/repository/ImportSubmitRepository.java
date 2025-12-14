package ua.ohonovskiy.onlineshop.synchronize.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.ohonovskiy.onlineshop.synchronize.enums.ImportStatus;
import ua.ohonovskiy.onlineshop.synchronize.model.ImportSubmit;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImportSubmitRepository extends JpaRepository<ImportSubmit, Long> {
    @Query("select s from ImportSubmit s where s.importStatus = :status order by s.createdAt asc")
    List<ImportSubmit> findOldestByStatus(@Param("status") ImportStatus status, Pageable pageable);

    default Optional<ImportSubmit> findEldestAccepted() {
        List<ImportSubmit> list = findOldestByStatus(ImportStatus.STATUS_ACCEPTED, Pageable.ofSize(1));
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Query("""
    select s from ImportSubmit s
    join fetch s.shop sh
    where s.id = :id
""")
    Optional<ImportSubmit> findByIdWithShop(@Param("id") Long id);

    // Атомарно "захоплюємо" задачу: ACCEPTED -> PROCESSING
    @Modifying
    @Query("""
        update ImportSubmit s
           set s.importStatus = :toStatus,
               s.startedAt = CURRENT_TIMESTAMP
         where s.id = :id and s.importStatus = :fromStatus
    """)
    int claim(@Param("id") Long id,
              @Param("fromStatus") ImportStatus fromStatus,
              @Param("toStatus") ImportStatus toStatus);
}
