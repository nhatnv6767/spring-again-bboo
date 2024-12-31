package com.ra.controller;

import com.ra.configuration.Translator;
import com.ra.dto.request.UserRequestDTO;
import com.ra.dto.response.*;
import com.ra.exception.ResourceNotFoundException;
import com.ra.service.UserService;
import com.ra.util.UserStatus;
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

    @Operation(summary = "Update an existing user", description = "API to update an existing user")
    @PutMapping("/{userId}")
    public ResponseData<?> updateUser(@Valid @RequestBody UserRequestDTO userRequestDTO,
                                      @Min(1) @PathVariable long userId) {
        log.info("Request to update user with ID {}: {}", userId, userRequestDTO);
        try {
            userService.updateUser(userId, userRequestDTO);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocale("user.update.success"), null);
        } catch (ResourceNotFoundException e) {
            log.error("Error while updating user : {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }

    @Operation(summary = "Change user status", description = "API to change the status of an existing user")
    @PatchMapping("/{userId}")
    public ResponseData<?> changeStatus(@Min(1) @RequestParam(required = false) UserStatus status,
                                        @Min(1) @PathVariable long userId) {
        log.info("Request to change status of user ID {} to {}", userId, status);
        try {
            userService.changeStatus(userId, status);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocale("user.update.success"), null);
        } catch (ResourceNotFoundException e) {
            log.error("Error while changing status of user : {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }

    @Operation(summary = "Delete a user", description = "API to delete an existing user")
    @DeleteMapping("/{userId}")
    public ResponseData<?> deleteUser(@Min(1) @PathVariable long userId) {
        log.info("Request to delete user with ID: {}", userId);
        try {
            userService.deleteUser(userId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), Translator.toLocale("user.delete.success"), null);
        } catch (ResourceNotFoundException e) {
            log.error("Error while deleting user : {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Get user details", description = "API to get details of a specific user")
    @GetMapping("/{userId}")
    public ResponseData<UserDetailResponse> getUser(@PathVariable long userId) {
        log.info("Request to get user with ID: {}", userId);

        try {
            return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("user.get.success"),
                    userService.getUser(userId));
        } catch (ResourceNotFoundException e) {
            log.error("Error while getting user : {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }

    @Operation(summary = "Get all users", description = "API to get list of all users with pagination and email filter")
    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<List<UserDetailResponse>> getAllUsers(
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int pageNo,
            @Min(10) @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String sortBy
    ) {
        log.info("Request to get all users with email: {}, pageNo: {}, pageSize: {}, sortBy: {}", email, pageNo, pageSize, sortBy);
        return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("user.getall.success"),
                userService.getAllUsersWithSortBy(pageNo, pageSize, sortBy));
    }


    @Operation(summary = "Get all users with multiple columns", description = "API to get list of all users with pagination and sorting by multiple columns")
    @GetMapping("/list-multiple")
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<List<UserDetailResponse>> getAllUsersWithSortByMultipleColumn(
            @RequestParam(defaultValue = "0") int pageNo,
            @Min(10) @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String... sorts
    ) {
        log.info("Request to get all users with multiple columns pageNo: {}, pageSize: {}, sortBy: {}", pageNo, pageSize, sorts);
        return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("user.getall.success"),
                userService.getAllUsersWithSortByMultipleColumn(pageNo, pageSize, sorts));
    }
}
