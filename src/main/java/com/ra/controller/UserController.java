package com.ra.controller;

import com.ra.dto.request.UserRequestDTO;
import com.ra.dto.response.ResponseData;
import com.ra.dto.response.ResponseSuccess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping(value = "/")
    public ResponseData<Integer> addUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return new ResponseData<>(HttpStatus.CREATED.value(), "User added successfully", 1);
    }

    @PutMapping("/{userId}")
    public ResponseData<?> updateUser(@Valid @RequestBody UserRequestDTO userRequestDTO, @PathVariable int userId) {
        System.out.println("User ID: " + userId);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User updated successfully", null);
    }

    @PatchMapping("/{userId}")
    public ResponseData<?> changeStatus(@Min(1) @RequestParam(required = false) int status,
                                        @Min(1) @PathVariable int userId) {
        System.out.println("Request to change status of user ID: " + userId + " to " + status);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Status changed successfully", null);
    }

    @DeleteMapping("/{userId}")
    public ResponseData<?> deleteUser(@Min(1) @PathVariable int userId) {
        System.out.println("Request to delete user ID: " + userId);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "User deleted successfully", null);
    }

    @GetMapping("/{userId}")
    public ResponseData<UserRequestDTO> getUser(@PathVariable int userId) {
        System.out.println("Request to get user ID: " + userId);
        return new ResponseData<>(HttpStatus.OK.value(), "User retrieved successfully",
                UserRequestDTO.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .email("john.doe@example.com")
                        .phone("1234567890")
                        .build());
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<List<UserRequestDTO>> getAllUsers(
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        System.out.println("Request to get all users");
        return new ResponseData<>(HttpStatus.OK.value(), "Users retrieved successfully",
                List.of(
                        UserRequestDTO.builder()
                                .firstName("John")
                                .lastName("Doe")
                                .email("john.doe@example.com")
                                .phone("1234567890")
                                .build(),
                        UserRequestDTO.builder()
                                .firstName("Jane")
                                .lastName("Smith")
                                .email("jane.smith@example.com")
                                .phone("9876543210")
                                .build()));
    }
}
