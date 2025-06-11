package com.project.Employee_Management.Backend.Controller;

import com.project.Employee_Management.Backend.Model.User;
import com.project.Employee_Management.Backend.Service.UserService;
import com.project.Employee_Management.Backend.dto.EmployeeDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final UserService userService;

    public EmployeeController(UserService userService) {
        this.userService = userService;
    }

    // Get all employees (Admin only)
    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<User> users = userService.getAllEmployees();
        List<EmployeeDto> dtos = users.stream()
                .map(userService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Get profile for logged-in user (Employee or Admin)
    @GetMapping("/me")
    public ResponseEntity<EmployeeDto> getMyProfile() {
        User currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(userService.convertToDto(currentUser));
    }
}
