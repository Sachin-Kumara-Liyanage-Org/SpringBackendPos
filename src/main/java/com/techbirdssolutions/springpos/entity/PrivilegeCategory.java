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
public class PrivilegeCategory{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "privilegeCategory")
    private Collection<Privilege> privileges;


}
