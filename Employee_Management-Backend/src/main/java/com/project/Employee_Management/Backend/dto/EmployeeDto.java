package com.project.Employee_Management.Backend.dto;

import com.project.Employee_Management.Backend.Model.Enum.role;
import lombok.Data;

@Data
public class EmployeeDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String department;
    private role Role;
}
