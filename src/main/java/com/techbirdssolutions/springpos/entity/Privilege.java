package com.techbirdssolutions.springpos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Privilege{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "privilege_category_id")
    private PrivilegeCategory privilegeCategory;

    private boolean superAdminOnly;

    @ManyToMany(mappedBy = "privileges", cascade = CascadeType.ALL)
    private Collection<Role> roles;
}
