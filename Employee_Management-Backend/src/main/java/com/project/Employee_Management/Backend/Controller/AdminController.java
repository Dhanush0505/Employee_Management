package com.project.Employee_Management.Backend.Controller;

import com.project.Employee_Management.Backend.Model.User;
import com.project.Employee_Management.Backend.Service.UserService;
import com.project.Employee_Management.Backend.dto.EmployeeDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")  // All endpoints require admin role
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // Get all users (employees)
    @GetMapping("/users")
    public ResponseEntity<List<EmployeeDto>> getAllUsers() {
        List<User> users = userService.getAllEmployees();
        List<EmployeeDto> dtos = users.stream().map(userService::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Get user by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<EmployeeDto> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userService.convertToDto(user));
    }

    @PostMapping("/adduser")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        BCryptPasswordEncoder encryter = new BCryptPasswordEncoder(12);
        user.setPassword(encryter.encode(user.getPassword()));
        User savedUser = userService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
    // Update user details
    @PutMapping("/users/{id}")
    public ResponseEntity<EmployeeDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDto employeeDto) {
        User updatedUser = userService.updateUser(id, employeeDto);
        return ResponseEntity.ok(userService.convertToDto(updatedUser));
    }

    // Delete user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
