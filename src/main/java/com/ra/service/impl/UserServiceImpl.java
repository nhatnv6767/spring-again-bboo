package com.ra.service.impl;

import com.ra.dto.request.UserRequestDTO;
import com.ra.exception.ResourceNotFoundException;
import com.ra.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public int addUser(UserRequestDTO requestDTO) {
        System.out.println("User added successfully to the database");
        if (requestDTO.getFirstName().equals("John")) {
            throw new ResourceNotFoundException("User with name John already exists");
        }
        return 0;
    }
}
