package com.learnwithme.blog.devblog.service;

import com.learnwithme.blog.devblog.dto.UserDto;

import java.util.List;



public interface UserService {

    /**
     * Get user by ID
     * @param userId the ID of the user
     * @return the user if found
     */
    UserDto getUserById(String userId);

    /**
     * Get user by username
     * @param username the username of the user
     * @return the user if found
     */
    UserDto getUserByUsername(String username);

    /**
     * Get all users
     * @return list of all users
     */
    List<UserDto> getAllUsers();

    /**
     * Update user
     * @param userId the ID of the user to update
     * @param userDto the updated user data
     * @return the updated user
     */
    UserDto updateUser(String userId, UserDto userDto);

    /**
     * Delete user
     * @param userId the ID of the user to delete
     */
    void deleteUser(String userId);

    /**
     * Update user's profile
     * @param userId the ID of the user
     * @param userDto the updated profile data
     * @return the updated user
     */
    UserDto updateProfile(String userId, UserDto userDto);

    /**
     * Change user's password
     * @param userId the ID of the user
     * @param oldPassword the current password
     * @param newPassword the new password
     * @return true if password was changed successfully
     */
    boolean changePassword(String userId, String oldPassword, String newPassword);

    /**
     * Activate or deactivate a user account
     * @param userId the ID of the user
     * @param active true to activate, false to deactivate
     * @return the updated user
     */
    UserDto setActiveStatus(String userId, boolean active);
}
