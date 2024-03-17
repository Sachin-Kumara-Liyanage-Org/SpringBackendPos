package com.techbirdssolutions.springpos.entity;

import com.techbirdssolutions.springpos.entity.customenum.MetadataTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaSettings extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    @Enumerated(EnumType.STRING)
    private MetadataTypes type = MetadataTypes.STRING;
    private String string;
    private boolean bool;
    private Long number;
    private LocalDateTime date;
}
