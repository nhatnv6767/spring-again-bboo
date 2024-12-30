package com.ra.controller;

import com.ra.configuration.Translator;
import com.ra.dto.request.UserRequestDTO;
import com.ra.dto.response.*;
import com.ra.service.UserService;
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
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/")
    public ResponseData<Integer> addUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        log.info("Request to add user: {}", userRequestDTO);
        try {
            userService.addUser(userRequestDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), Translator.toLocale("user.add.success"), 1);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), Translator.toLocale("user.add.error"), e.getMessage());
        }
    }

    @PutMapping("/{userId}")
    public ResponseData<?> updateUser(@Valid @RequestBody UserRequestDTO userRequestDTO,
                                      @Min(1) @PathVariable int userId) {
        log.info("Request to update user with ID {}: {}", userId, userRequestDTO);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocale("user.update.success"), null);
    }

    @PatchMapping("/{userId}")
    public ResponseData<?> changeStatus(@Min(1) @RequestParam(required = false) int status,
                                        @Min(1) @PathVariable int userId) {
        log.info("Request to change status of user ID {} to {}", userId, status);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocale("user.update.success"), null);
    }

    @DeleteMapping("/{userId}")
    public ResponseData<?> deleteUser(@Min(1) @PathVariable int userId) {
        log.info("Request to delete user with ID: {}", userId);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), Translator.toLocale("user.delete.success"), null);
    }

    @GetMapping("/{userId}")
    public ResponseData<UserRequestDTO> getUser(@PathVariable int userId) {
        log.info("Request to get user with ID: {}", userId);
        return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("user.get.success"),
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
            @Min(10) @RequestParam(defaultValue = "10") int pageSize) {
        log.info("Request to get all users with email: {}, pageNo: {}, pageSize: {}", email, pageNo, pageSize);
        return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("user.getall.success"),
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
