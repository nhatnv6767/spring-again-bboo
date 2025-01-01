package com.ra.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "group")
    private Set<GroupHasUser> groups = new HashSet<>();
}
