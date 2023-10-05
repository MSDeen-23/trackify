package com.beworkerbee.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Table(name = "organization")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Organization extends BaseEntity {

    @Column(unique = true)
    private String organizationName;

    @OneToOne
    private User adminUser;

    @OneToMany(mappedBy = "organization",cascade = CascadeType.ALL)
    private Set<User> users;
}
