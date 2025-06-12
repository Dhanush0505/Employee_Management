package com.project.Employee_Management.Backend.Controller;

import com.project.Employee_Management.Backend.Model.User;
import com.project.Employee_Management.Backend.Service.LeaveService;
import com.project.Employee_Management.Backend.Service.UserService;
import com.project.Employee_Management.Backend.dto.EmployeeDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")  // All endpoints require admin role
public class AdminController {

    private final UserService userService;
    private final LeaveService leaveService;
    public AdminController(UserService userService,  LeaveService leaveService) {
        this.userService = userService;
        this.leaveService = leaveService;
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
    @GetMapping("/user/profile")
    public ResponseEntity<EmployeeDto> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User user = userService.findByUsername(currentUsername);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        EmployeeDto dto = userService.convertToDto(user);
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/user/employee-count")
    public ResponseEntity<Map<String, Long>> getEmployeeCount() {
        long count = userService.countAllEmployees();
        Map<String, Long> response = new HashMap<>();
        response.put("totalEmployees", count);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getDashboardStats() {
        long totalUsers = userService.countAllEmployees();
        long pendingLeaves = leaveService.countLeavesByStatus("Pending");

        Map<String, Long> stats = new HashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("pendingLeaves", pendingLeaves);

        return ResponseEntity.ok(stats);
    }
    @GetMapping("/profile")
    public ResponseEntity<Map<String, String>> getAdminProfile(@AuthenticationPrincipal UserDetails userDetails) {

        Map<String, String> profile = new HashMap<>();
        profile.put("username", userDetails.getUsername());
        return ResponseEntity.ok(profile);
    }
    // Delete user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
