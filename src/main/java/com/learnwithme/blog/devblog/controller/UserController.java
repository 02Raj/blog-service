package com.learnwithme.blog.devblog.controller;

import java.util.List;

import com.learnwithme.blog.devblog.dto.ApiResponseDto;
import com.learnwithme.blog.devblog.dto.PasswordChangeDto;
import com.learnwithme.blog.devblog.dto.UserDto;
import com.learnwithme.blog.devblog.exception.UnauthorizedException;
import com.learnwithme.blog.devblog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Get current user profile
     * @return current user
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponseDto<UserDto>> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        UserDto userDto = userService.getUserByUsername(username);

        ApiResponseDto<UserDto> response = ApiResponseDto.success(
                userDto,
                "User profile retrieved successfully",
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Update current user profile
     * @param userDto updated profile data
     * @return updated user
     */
    @PutMapping("/me")
    public ResponseEntity<ApiResponseDto<UserDto>> updateProfile(@Valid @RequestBody UserDto userDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        UserDto currentUser = userService.getUserByUsername(username);
        UserDto updatedUser = userService.updateProfile(currentUser.getId(), userDto);

        ApiResponseDto<UserDto> response = ApiResponseDto.success(
                updatedUser,
                "Profile updated successfully",
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Change current user password
     * @param passwordChangeDto old and new password
     * @return success status
     */
    @PutMapping("/me/password")
    public ResponseEntity<ApiResponseDto<Object>> changePassword(@Valid @RequestBody PasswordChangeDto passwordChangeDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        UserDto currentUser = userService.getUserByUsername(username);

        boolean success = userService.changePassword(
                currentUser.getId(),
                passwordChangeDto.getOldPassword(),
                passwordChangeDto.getNewPassword());

        if (success) {
            ApiResponseDto<Object> response = ApiResponseDto.success(
                    "Password changed successfully",
                    HttpStatus.OK.value()
            );

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new UnauthorizedException("Invalid current password");
        }
    }

    /**
     * Get all users (admin only)
     * @return list of all users
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<List<UserDto>>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();

        ApiResponseDto<List<UserDto>> response = ApiResponseDto.success(
                users,
                "Users retrieved successfully",
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get user by ID (admin only)
     * @param userId user ID
     * @return user if found
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<UserDto>> getUserById(@PathVariable String userId) {
        UserDto user = userService.getUserById(userId);

        ApiResponseDto<UserDto> response = ApiResponseDto.success(
                user,
                "User retrieved successfully",
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Update user (admin only)
     * @param userId user ID
     * @param userDto updated user data
     * @return updated user
     */
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<UserDto>> updateUser(@PathVariable String userId, @Valid @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(userId, userDto);

        ApiResponseDto<UserDto> response = ApiResponseDto.success(
                updatedUser,
                "User updated successfully",
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Delete user (admin only)
     * @param userId user ID
     * @return success status
     */
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<Object>> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);

        ApiResponseDto<Object> response = ApiResponseDto.success(
                "User deleted successfully",
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Activate or deactivate user (admin only)
     * @param userId user ID
     * @param active active status
     * @return updated user
     */
    @PutMapping("/{userId}/status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<UserDto>> setUserStatus(@PathVariable String userId, @RequestBody boolean active) {
        UserDto updatedUser = userService.setActiveStatus(userId, active);

        String statusMessage = active ? "User activated successfully" : "User deactivated successfully";

        ApiResponseDto<UserDto> response = ApiResponseDto.success(
                updatedUser,
                statusMessage,
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
