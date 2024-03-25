package com.techbirdssolutions.springpos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;
/**
 * This class represents the User entity in the application.
 * It extends the Auditable class to inherit fields and behavior for auditing purposes.
 * The class is annotated with @Entity, indicating that it is a JPA entity.
 * Lombok annotations are used to reduce boilerplate code for getters, setters, constructors, and equals/hashCode methods.
 * The class contains fields for id, firstName, lastName, email, phoneNumber, refreshToken, password, display, printer, lang, enabled, and roles.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class User extends Auditable{
    /**
     * The unique identifier for a User entity.
     * It is generated automatically by the JPA provider.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The first name of the User entity.
     */
    private String firstName;
    /**
     * The last name of the User entity.
     */
    private String lastName;
    /**
     * The email of the User entity.
     * It is unique across all User entities.
     */
    @Column(unique = true)
    private String email;
    /**
     * The phone number of the User entity.
     */
    private String phoneNumber;
    /**
     * The refresh token of the User entity.
     */
    private String refreshToken;
    /**
     * The password reset token of the User entity.
     */
    private String passwordResetToken;
    /**
     * The password of the User entity.
     */
    private String password;
    /**
     * The display setting of the User entity.
     */
    private String display;
    /**
     * The printer setting of the User entity.
     */
    private String printer;
    /**
     * The language setting of the User entity.
     */
    private String lang;
    /**
     * A flag indicating whether the User entity is enabled.
     */
    private boolean enabled;
    /**
     * The roles that the User entity has.
     * It is a many-to-many relationship with the Role entity.
     * The relationship is managed through a join table named "users_roles".
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

}
