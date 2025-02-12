package com.ra.controller;

import com.ra.configuration.Translator;
import com.ra.dto.request.UserRequestDTO;
import com.ra.dto.response.*;
import com.ra.exception.ResourceNotFoundException;
import com.ra.service.UserService;
import com.ra.util.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

    // confirm api (confirm a new user with secret code)
    @Operation(summary = "Confirm a new user", description = "API to confirm a new user with secret code")
    @GetMapping("/confirm/{userId}")
    public ResponseData<?> confirmUser(@PathVariable long userId, @RequestParam String secretCode, HttpServletResponse response) throws IOException {
        log.info("Request to confirm user with ID: {}", userId);
        try {
            userService.confirmUser(userId, secretCode);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocale("user.confirm.success"), null);
        } catch (ResourceNotFoundException e) {
            log.error("Error while confirming user : {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } finally {
            // direct to login page
            response.sendRedirect("https://www.google.com");

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
    public ResponseData<?> getAllUsers(
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
    public ResponseData<?> getAllUsersWithSortByMultipleColumn(
            @RequestParam(defaultValue = "0") int pageNo,
            @Min(0) @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String... sorts
    ) {
        log.info("Request to get all users with multiple columns pageNo: {}, pageSize: {}, sortBy: {}", pageNo, pageSize, sorts);
        return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("user.getall.success"),
                userService.getAllUsersWithSortByMultipleColumns(pageNo, pageSize, sorts));
    }

    @Operation(summary = "Get all users with search", description = "API to get list of all users with pagination and search")
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<?> getAllUsersWithSearch(
            @RequestParam(defaultValue = "0") int pageNo,
            @Min(1) @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy
    ) {
        log.info("Request to get all users with search: {}, pageNo: {}, pageSize: {}, sortBy: {}", search, pageNo, pageSize, sortBy);
        return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("user.getall.success"),
                userService.getAllUsersWithSortByColumnAndSearch(pageNo, pageSize, search, sortBy));
    }

    // Spring Data JPA - Criteria - Search
    @Operation(summary = "Get all users with search using Criteria", description = "API to get list of all users with pagination and search using Criteria")
    @GetMapping("/search-criteria")
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<?> advanceSearchByCriteria(
            @RequestParam(defaultValue = "0") int pageNo,
            @Min(1) @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String address,
            @RequestParam(defaultValue = "") String... search
    ) {
        log.info("Request to get all users with search using Criteria: {}, pageNo: {}, pageSize: {}, sortBy: {}", search, pageNo, pageSize, sortBy);
        return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("user.getall.success"),
                userService.advanceSearchByCriteria(pageNo, pageSize, sortBy, address, search));
    }

    // Spring Data JPA - Search Specification
    @Operation(summary = "Get all users with search using Specification", description = "API to get list of all users with pagination and search using Specification")
    @GetMapping("/search-specification")
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<?> advanceSearchBySpecification(
            Pageable pageable,
            @RequestParam(required = false) String[] user,
            @RequestParam(required = false) String[] address
    ) {
        return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("user.getall.success"),
                userService.advanceSearchBySpecification(pageable, user, address));
    }
}
