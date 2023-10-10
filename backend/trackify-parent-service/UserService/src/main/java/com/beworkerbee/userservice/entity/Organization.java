package com.beworkerbee.userservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "organization")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Organization extends BaseEntity implements Serializable {

    @Column(unique = true)
    private String organizationName;

    @OneToOne
    @JsonIgnoreProperties({"organization"})
    private User adminUser;

}
