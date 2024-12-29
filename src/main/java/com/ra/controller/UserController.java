package com.ra.controller;

import com.ra.dto.request.UserRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping(value = "/")
    public String addUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return "User added successfully";
    }

    @PutMapping("/{userId}")
    public String updateUser(@Valid @RequestBody UserRequestDTO userRequestDTO, @PathVariable int userId) {
        System.out.println("User ID: " + userId);
        return "User updated successfully";

    }

    @PatchMapping("/{userId}")
    public String changeStatus(@Min(1) @RequestParam(required = false) int status, @Min(1) @PathVariable int userId) {
        System.out.println("Request to change status of user ID: " + userId + " to " + status);
        return "User status changed successfully, status: " + status;

    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@Min(1) @PathVariable int userId) {
        System.out.println("Request to delete user ID: " + userId);
        return "User deleted successfully, ID: " + userId;
    }

    @GetMapping("/{userId}")
    public UserRequestDTO getUser(@PathVariable int userId) {
        System.out.println("Request to get user ID: " + userId);
        return UserRequestDTO.builder().firstName("John").lastName("Doe").email("john.doe@example.com")
                .phone("1234567890").build();

    }

    @GetMapping("/list")
    public List<UserRequestDTO> getAllUsers(
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        System.out.println("Request to get all users");
        return List.of(
                UserRequestDTO.builder().firstName("John").lastName("Doe").email("john.doe@example.com")
                        .phone("1234567890").build(),
                UserRequestDTO.builder().firstName("Jane").lastName("Smith").email("jane.smith@example.com")
                        .phone("9876543210").build());
    }
}
