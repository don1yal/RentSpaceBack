package com.rentspace.userservice.controller;

import com.rentspace.userservice.dto.UserCreateDto;
import com.rentspace.userservice.dto.UserResponseDto;
import com.rentspace.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@Tag(name = "Users REST API in RentSpace",
        description = "REST APIs in RentSpace to CREATE, READ, UPDATE and DELETE users")
@RestController
@RequestMapping("/api/v1/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            summary ="Get user by email",
            description = "Get User by Email inside RentSpace"
    )
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable @Email @NotBlank String email) {
        return ResponseEntity
                .status(OK)
                .body(userService.getUserByEmail(email));
    }

    @Operation(
            summary ="Get user by ID",
            description = "Get User by ID inside RentSpace"
    )
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        return ResponseEntity
                .status(OK)
                .body(userService.getUserById(userId));
    }

    @Operation(
            summary ="Create user",
            description = "Create User inside RentSpace"
    )
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        return ResponseEntity
                .status(CREATED)
                .body(userService.createUser(userCreateDto));
    }

    @Operation(
            summary ="Update user",
            description = "Update User inside RentSpace"
    )
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long userId,
                                                      @Valid @RequestBody UserCreateDto userCreateDto) {
        return ResponseEntity
                .status(OK)
                .body(userService.updateUser(userId, userCreateDto));
    }

    @Operation(
            summary ="Delete user",
            description = "Delete User inside RentSpace"
    )
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity
                .status(NO_CONTENT)
                .build();
    }
}
