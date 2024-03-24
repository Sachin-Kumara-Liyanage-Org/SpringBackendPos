package com.techbirdssolutions.springpos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;
/**
 * This class represents the Role entity in the application.
 * It extends the Auditable class to inherit fields and behavior for auditing purposes.
 * The class is annotated with @Entity, indicating that it is a JPA entity.
 * Lombok annotations are used to reduce boilerplate code for getters, setters, constructors, and equals/hashCode methods.
 * The class contains fields for id, name, users, and privileges.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Role extends Auditable{
    /**
     * The unique identifier for a Role entity.
     * It is generated automatically by the JPA provider.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The name of the Role entity.
     * It is unique across all Role entities.
     */
    @Column(unique = true)
    private String name;
    /**
     * The users that have this role.
     * It is a many-to-many relationship with the User entity.
     */
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;
    /**
     * The privileges that belong to this role.
     * It is a many-to-many relationship with the Privilege entity.
     * The relationship is managed through a join table named "roles_privileges".
     */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"))
    private Collection<Privilege> privileges;
    /**
     * A constructor that initializes the Role entity with a name.
     * @param name The name of the Role entity.
     */
    public Role(String name) {
        this.name = name;
    }
}