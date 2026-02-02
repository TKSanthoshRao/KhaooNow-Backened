package com.food.khaaonow.model.auth;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "permissions",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_permissions_name",
                columnNames = "name"
        )
)
@Getter
@Setter
@NoArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 150)
    private String description;

    @Column(nullable = false)
    private Boolean active;
}
