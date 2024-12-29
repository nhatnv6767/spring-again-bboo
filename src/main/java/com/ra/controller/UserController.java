package com.ra.controller;

import com.ra.dto.request.UserRequestDTO;
import com.ra.dto.response.ResponseSuccess;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping(value = "/")
    public ResponseSuccess addUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return new ResponseSuccess(HttpStatus.CREATED, "User added successfully", 1);
    }

    @PutMapping("/{userId}")
    public ResponseSuccess updateUser(@Valid @RequestBody UserRequestDTO userRequestDTO, @PathVariable int userId) {
        System.out.println("User ID: " + userId);
        return new ResponseSuccess(HttpStatus.ACCEPTED, "User updated successfully");
    }

    @PatchMapping("/{userId}")
    public ResponseSuccess changeStatus(@Min(1) @RequestParam(required = false) int status,
            @Min(1) @PathVariable int userId) {
        System.out.println("Request to change status of user ID: " + userId + " to " + status);
        return new ResponseSuccess(HttpStatus.ACCEPTED, "Status changed successfully");

    }

    @DeleteMapping("/{userId}")
    public ResponseSuccess deleteUser(@Min(1) @PathVariable int userId) {
        System.out.println("Request to delete user ID: " + userId);
        return new ResponseSuccess(HttpStatus.NO_CONTENT, "User deleted successfully");
    }

    @GetMapping("/{userId}")
    public ResponseSuccess getUser(@PathVariable int userId) {
        System.out.println("Request to get user ID: " + userId);
        // return
        // UserRequestDTO.builder().firstName("John").lastName("Doe").email("john.doe@example.com")
        // .phone("1234567890").build();
        return new ResponseSuccess(HttpStatus.OK, "User retrieved successfully", UserRequestDTO.builder()
                .firstName("John").lastName("Doe").email("john.doe@example.com").phone("1234567890").build());

    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public ResponseSuccess getAllUsers(
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        System.out.println("Request to get all users");
        // return List.of(
        // UserRequestDTO.builder().firstName("John").lastName("Doe").email("john.doe@example.com")
        // .phone("1234567890").build(),
        // UserRequestDTO.builder().firstName("Jane").lastName("Smith").email("jane.smith@example.com")
        // .phone("9876543210").build());
        return new ResponseSuccess(HttpStatus.OK, "Users retrieved successfully", List.of(
                UserRequestDTO.builder().firstName("John").lastName("Doe").email("john.doe@example.com")
                        .phone("1234567890").build(),
                UserRequestDTO.builder().firstName("Jane").lastName("Smith").email("jane.smith@example.com")
                        .phone("9876543210").build()));
    }
}
