package com.project.Employee_Management.Backend.Service;

import com.project.Employee_Management.Backend.Model.Enum.role;
import com.project.Employee_Management.Backend.Model.User;
import com.project.Employee_Management.Backend.Repository.UserRepository;
import com.project.Employee_Management.Backend.dto.EmployeeDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Get all employees (excluding admins if you want)
    public List<User> getAllEmployees() {
        return userRepository.findAll(); // or filter by role if needed
    }

    // Get user by ID
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    // Update user details
    public User updateUser(Long id, EmployeeDto employeeDto) {
        User user = getUserById(id);
        user.setEmail(employeeDto.getEmail());
        user.setDepartment(employeeDto.getDepartment());
        // For role updates, add logic carefully
        userRepository.save(user);
        return user;
    }

    // Delete user by ID
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Convert User entity to DTO
    public EmployeeDto convertToDto(User user) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setDepartment(user.getDepartment());
        dto.setRole(role.valueOf(user.getRole().name()));
        return dto;
    }
    public User saveUser(User user) {
        // Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        // Save user
        return userRepository.save(user);
    }
    // Get currently authenticated user
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public User findByUsername(String currentUsername) {
        return userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + currentUsername));
    }

    public long countAllEmployees() {
        return userRepository.count();
    }
}
