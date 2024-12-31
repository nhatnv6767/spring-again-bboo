package com.ra.controller;

import com.ra.configuration.Translator;
import com.ra.dto.request.UserRequestDTO;
import com.ra.dto.response.*;
import com.ra.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User", description = "User management")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Add a new user", description = "API to add a new user")
    @PostMapping(value = "/")
    public ResponseData<Long> addUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        log.info("Request to add user: {}", userRequestDTO);
        try {
            long userId = userService.saveUser(userRequestDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), Translator.toLocale("user.add.success"), userId);
        } catch (Exception e) {
            log.error("Error while adding user : {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), Translator.toLocale("user.add.error"),
                    e.getMessage());
        }
    }

    //    @Operation(summary = "Update an existing user", description = "API to update an existing user")
//    @PutMapping("/{userId}")
//    public ResponseData<?> updateUser(@Valid @RequestBody UserRequestDTO userRequestDTO,
//                                      @Min(1) @PathVariable int userId) {
//        log.info("Request to update user with ID {}: {}", userId, userRequestDTO);
//        return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocale("user.update.success"), null);
//    }
//
//    @Operation(summary = "Change user status", description = "API to change the status of an existing user")
//    @PatchMapping("/{userId}")
//    public ResponseData<?> changeStatus(@Min(1) @RequestParam(required = false) int status,
//                                        @Min(1) @PathVariable int userId) {
//        log.info("Request to change status of user ID {} to {}", userId, status);
//        return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocale("user.update.success"), null);
//    }
//
//    @Operation(summary = "Delete a user", description = "API to delete an existing user")
//    @DeleteMapping("/{userId}")
//    public ResponseData<?> deleteUser(@Min(1) @PathVariable int userId) {
//        log.info("Request to delete user with ID: {}", userId);
//        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), Translator.toLocale("user.delete.success"), null);
//    }
//
//    @Operation(summary = "Get user details", description = "API to get details of a specific user")
//    @GetMapping("/{userId}")
//    public ResponseData<UserRequestDTO> getUser(@PathVariable int userId) {
//        log.info("Request to get user with ID: {}", userId);
//        return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("user.get.success"),
//                UserRequestDTO.builder()
//                        .firstName("John")
//                        .lastName("Doe")
//                        .email("john.doe@example.com")
//                        .phone("1234567890")
//                        .build());
//    }
//
    @Operation(summary = "Get all users", description = "API to get list of all users with pagination and email filter")
    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<List<UserDetailResponse>> getAllUsers(
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int pageNo,
            @Min(10) @RequestParam(defaultValue = "10") int pageSize) {
        log.info("Request to get all users with email: {}, pageNo: {}, pageSize: {}", email, pageNo, pageSize);
        return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("user.getall.success"),
                userService.getAllUsers(pageNo, pageSize));
    }
}
