package com.ra.service;

import com.ra.model.Role;
import com.ra.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record RoleService(RoleRepository roleRepository) {

    @PostConstruct
    public List<Role> findAll() {
        List<Role> roles = roleRepository.getAllByUserId(2l);
        return roles;
    }
}
