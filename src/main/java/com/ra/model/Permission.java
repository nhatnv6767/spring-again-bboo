package com.ra.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_permission")
public class Permission extends AbstractEntity<Integer> {
    private String name;
    private String description;
}
