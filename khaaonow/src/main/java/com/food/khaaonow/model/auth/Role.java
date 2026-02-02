package com.food.khaaonow.model.auth;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "roles", uniqueConstraints = @UniqueConstraint(name = "uk_roles_name",columnNames =
"name"))
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 50)
    private String name;

    @Column(length = 150)
    private String description;

    @Column(nullable = false)
    private Boolean active;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_permissions",joinColumns = @JoinColumn(name = "role_id"),inverseJoinColumns = @JoinColumn(name = "permission_id"),uniqueConstraints = @UniqueConstraint(name = "uk_role_permission",columnNames = {"role_id","permission_id"}))
    private Set<Permission> permissions = new HashSet<>();
}
