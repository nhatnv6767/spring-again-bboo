package com.ra.controller;

import com.ra.dto.request.UserRequestDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/")
    public String addUser(@RequestBody UserRequestDTO userRequestDTO) {
        return "User added successfully";
    }

    @PutMapping("/{userId}")
    public String updateUser(@RequestBody UserRequestDTO userRequestDTO, @PathVariable int userId) {
        System.out.println("User ID: " + userId);
        return "User updated successfully";

    }

    @PatchMapping("/{userId}")
    public String changeStatus(@RequestParam(required = false) boolean status, @PathVariable int userId) {
        System.out.println("Request to change status of user ID: " + userId + " to " + status);
        return "User status changed successfully, status: " + status;

    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable int userId) {
        System.out.println("Request to delete user ID: " + userId);
        return "User deleted successfully, ID: " + userId;
    }

    @GetMapping("/{userId}")
    public UserRequestDTO getUser(@PathVariable int userId) {
        System.out.println("Request to get user ID: " + userId);
        return UserRequestDTO.builder().firstName("John").lastName("Doe").email("john.doe@example.com")
                .phone("1234567890").build();

    }

}
