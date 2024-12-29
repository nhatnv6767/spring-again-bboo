package com.ra.controller;

import com.ra.dto.request.UserRequestDTO;
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

    @Operation(summary = "Add a new user", description = "Add a new user to the system", responses = {
            @ApiResponse(responseCode = "201", description = "User added successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(name = "User added successfully", summary = "User added successfully", value = "{\"status\":201,\"message\":\"User added successfully\",\"data\":1}"))),
    })
    @PostMapping(value = "/")
    public ResponseSuccess addUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return new ResponseSuccess(HttpStatus.CREATED, "User added successfully", 1);
    }

    @Operation(summary = "Update a user", description = "Update a user in the system", responses = {
            @ApiResponse(responseCode = "202", description = "User updated successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(name = "User updated successfully", summary = "User updated successfully", value = "{\"status\":202,\"message\":\"User updated successfully\",\"data\":null}"))),
    })
    @PutMapping("/{userId}")
    public ResponseSuccess updateUser(@Valid @RequestBody UserRequestDTO userRequestDTO, @PathVariable int userId) {
        System.out.println("User ID: " + userId);
        return new ResponseSuccess(HttpStatus.ACCEPTED, "User updated successfully");
    }

    @Operation(summary = "Change a user's status", description = "Change a user's status in the system", responses = {
            @ApiResponse(responseCode = "202", description = "Status changed successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(name = "Status changed successfully", summary = "Status changed successfully", value = "{\"status\":202,\"message\":\"Status changed successfully\",\"data\":null}"))),
    })
    @PatchMapping("/{userId}")
    public ResponseSuccess changeStatus(@Min(1) @RequestParam(required = false) int status,
            @Min(1) @PathVariable int userId) {
        System.out.println("Request to change status of user ID: " + userId + " to " + status);
        return new ResponseSuccess(HttpStatus.ACCEPTED, "Status changed successfully");

    }

    @Operation(summary = "Delete a user", description = "Delete a user from the system", responses = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(name = "User deleted successfully", summary = "User deleted successfully", value = "{\"status\":204,\"message\":\"User deleted successfully\",\"data\":null}"))),
    })
    @DeleteMapping("/{userId}")
    public ResponseSuccess deleteUser(@Min(1) @PathVariable int userId) {
        System.out.println("Request to delete user ID: " + userId);
        return new ResponseSuccess(HttpStatus.NO_CONTENT, "User deleted successfully");
    }

    @Operation(summary = "Get a user", description = "Get a user from the system", responses = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(name = "User retrieved successfully", summary = "User retrieved successfully", value = "{\"status\":200,\"message\":\"User retrieved successfully\",\"data\":{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"phone\":\"1234567890\"}}"))),
    })
    @GetMapping("/{userId}")
    public ResponseSuccess getUser(@PathVariable int userId) {
        System.out.println("Request to get user ID: " + userId);
        // return
        // UserRequestDTO.builder().firstName("John").lastName("Doe").email("john.doe@example.com")
        // .phone("1234567890").build();
        return new ResponseSuccess(HttpStatus.OK, "User retrieved successfully", UserRequestDTO.builder()
                .firstName("John").lastName("Doe").email("john.doe@example.com").phone("1234567890").build());

    }

    @Operation(summary = "Get all users", description = "Get all users from the system", responses = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(name = "Users retrieved successfully", summary = "Users retrieved successfully", value = "{\"status\":200,\"message\":\"Users retrieved successfully\",\"data\":[{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"phone\":\"1234567890\"},{\"firstName\":\"Jane\",\"lastName\":\"Smith\",\"email\":\"jane.smith@example.com\",\"phone\":\"9876543210\"}]}"))),
    })
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
