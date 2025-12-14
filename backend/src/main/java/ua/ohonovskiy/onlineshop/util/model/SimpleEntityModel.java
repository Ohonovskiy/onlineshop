package ua.ohonovskiy.onlineshop.util.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public abstract class SimpleEntityModel extends SimpleEntityModelNoId {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq_gen")
    @SequenceGenerator(name = "global_seq_gen", sequenceName = "global_seq", allocationSize = 50)
    protected Long id;
}
