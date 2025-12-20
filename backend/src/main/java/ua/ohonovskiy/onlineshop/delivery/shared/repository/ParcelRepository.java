package ua.ohonovskiy.onlineshop.delivery.shared.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ua.ohonovskiy.onlineshop.delivery.shared.model.Parcel;

@NoRepositoryBean
public interface ParcelRepository extends JpaRepository<Parcel, Long> {
}
