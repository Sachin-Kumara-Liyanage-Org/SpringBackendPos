package com.techbirdssolutions.springpos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
/**
 * This class represents the PrivilegeCategory entity in the application.
 * It extends the Auditable class to inherit fields and behavior for auditing purposes.
 * The class is annotated with @Entity, indicating that it is a JPA entity.
 * Lombok annotations are used to reduce boilerplate code for getters, setters, constructors, and equals/hashCode methods.
 * The class contains fields for id, name, and privileges.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PrivilegeCategory{
    /**
     * The unique identifier for a PrivilegeCategory entity.
     * It is generated automatically by the JPA provider.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The name of the PrivilegeCategory entity.
     * It is unique across all PrivilegeCategory entities.
     */
    @Column(unique = true)
    private String name;
    /**
     * The privileges that belong to this category.
     * It is a one-to-many relationship with the Privilege entity.
     */
    @OneToMany(mappedBy = "privilegeCategory")
    private Collection<Privilege> privileges;


}
