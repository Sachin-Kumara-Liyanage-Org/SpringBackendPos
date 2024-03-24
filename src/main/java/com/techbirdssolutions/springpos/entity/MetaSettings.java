package com.techbirdssolutions.springpos.entity;

import com.techbirdssolutions.springpos.entity.customenum.MetadataTypes;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
/**
 * This class represents the MetaSettings entity in the application.
 * It extends the Auditable class to inherit fields and behavior for auditing purposes.
 * The class is annotated with @Entity, indicating that it is a JPA entity.
 * Lombok annotations are used to reduce boilerplate code for getters, setters, constructors, and equals/hashCode methods.
 * The class contains fields for id, name, type, string, bool, number, and date.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class MetaSettings extends Auditable{
    /**
     * The unique identifier for a MetaSettings entity.
     * It is generated automatically by the JPA provider.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The name of the MetaSettings entity.
     * It is unique across all MetaSettings entities.
     */
    @Column(unique = true)
    private String name;
    /**
     * The type of the MetaSettings entity.
     * It is an enum of MetadataTypes and defaults to STRING.
     */
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MetadataTypes type = MetadataTypes.STRING;
    /**
     * The string value of the MetaSettings entity.
     * It is used when the type is STRING.
     */
    private String string;
    /**
     * The boolean value of the MetaSettings entity.
     * It is used when the type is BOOLEAN.
     */
    private boolean bool;
    /**
     * The numeric value of the MetaSettings entity.
     * It is used when the type is NUMBER.
     */
    private Long number;
    /**
     * The date and time value of the MetaSettings entity.
     * It is used when the type is DATE_TIME.
     */
    private LocalDateTime date;
}
