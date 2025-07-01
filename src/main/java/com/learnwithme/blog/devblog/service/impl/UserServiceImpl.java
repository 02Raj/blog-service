package com.learnwithme.blog.devblog.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.learnwithme.blog.devblog.dto.UserDto;
import com.learnwithme.blog.devblog.exception.ResourceNotFoundException;
import com.learnwithme.blog.devblog.exception.UnauthorizedException;
import com.learnwithme.blog.devblog.model.User;
import com.learnwithme.blog.devblog.repository.UserRepository;
import com.learnwithme.blog.devblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return mapToDto(user);
    }

    @Override
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return mapToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) {
        // Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Update fields
        user.setName(userDto.getName());

        // Update email if provided and different
        if (userDto.getEmail() != null && !userDto.getEmail().equals(user.getEmail())) {
            // Check if email already exists
            if (userRepository.existsByEmail(userDto.getEmail())) {
                throw new IllegalArgumentException("Email is already in use");
            }
            user.setEmail(userDto.getEmail());
        }

        // Update roles if provided
        if (userDto.getRoles() != null && !userDto.getRoles().isEmpty()) {
            user.setRoles(userDto.getRoles());
        }

        // Update profile picture if provided
        if (userDto.getProfilePicture() != null) {
            user.setProfilePicture(userDto.getProfilePicture());
        }

        // Update bio if provided
        if (userDto.getBio() != null) {
            user.setBio(userDto.getBio());
        }

        // Save updated user
        User updatedUser = userRepository.save(user);

        // Map to DTO and return
        return mapToDto(updatedUser);
    }

    @Override
    public void deleteUser(String userId) {
        // Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Delete user
        userRepository.delete(user);
    }

    @Override
    public UserDto updateProfile(String userId, UserDto userDto) {
        // Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Update profile fields
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        if (userDto.getBio() != null) {
            user.setBio(userDto.getBio());
        }

        if (userDto.getProfilePicture() != null) {
            user.setProfilePicture(userDto.getProfilePicture());
        }

        // Save updated user
        User updatedUser = userRepository.save(user);

        // Map to DTO and return
        return mapToDto(updatedUser);
    }

    @Override
    public boolean changePassword(String userId, String oldPassword, String newPassword) {
        // Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Verify old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new UnauthorizedException("Invalid current password");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));

        // Save user
        userRepository.save(user);

        return true;
    }

    @Override
    public UserDto setActiveStatus(String userId, boolean active) {
        // Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Update active status
        user.setActive(active);

        // Save user
        User updatedUser = userRepository.save(user);

        // Map to DTO and return
        return mapToDto(updatedUser);
    }

    // Helper method for mapping
    private UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .profilePicture(user.getProfilePicture())
                .bio(user.getBio())
                .active(user.isActive())
                .build();
    }
}