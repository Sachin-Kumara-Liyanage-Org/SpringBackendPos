package com.techbirdssolutions.springpos.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
/**
 * This class represents the Privilege entity in the application.
 * It extends the Auditable class to inherit fields and behavior for auditing purposes.
 * The class is annotated with @Entity, indicating that it is a JPA entity.
 * Lombok annotations are used to reduce boilerplate code for getters, setters, constructors, and equals/hashCode methods.
 * The class contains fields for id, name, privilegeCategory, superAdminOnly, and roles.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Privilege{
    /**
     * The unique identifier for a Privilege entity.
     * It is generated automatically by the JPA provider.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The name of the Privilege entity.
     * It is unique across all Privilege entities.
     */
    @Column(unique = true)
    private String name;
    /**
     * The category of the Privilege entity.
     * It is a many-to-one relationship with the PrivilegeCategory entity.
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "privilege_category_id")
    private PrivilegeCategory privilegeCategory;
    /**
     * A flag indicating whether the privilege is only for super admins.
     */
    private boolean superAdminOnly;
    /**
     * The roles that have this privilege.
     * It is a many-to-many relationship with the Role entity.
     */
    @ManyToMany(mappedBy = "privileges", cascade = CascadeType.ALL)
    @JsonIgnore
    transient private Collection<Role> roles;
}
