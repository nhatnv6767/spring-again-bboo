package com.ra.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_group")
public class Group extends AbstractEntity<Integer> {
    private String name;
    private String description;

    @OneToOne
    private Role role;
}
