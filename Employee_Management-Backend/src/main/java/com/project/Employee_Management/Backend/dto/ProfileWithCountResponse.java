package com.project.Employee_Management.Backend.dto;

public class ProfileWithCountResponse {
    private EmployeeDto employee;
    private long totalEmployees;

    public ProfileWithCountResponse(EmployeeDto employee, long totalEmployees) {
        this.employee = employee;
        this.totalEmployees = totalEmployees;
    }

    // Getters and setters
    public EmployeeDto getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDto employee) {
        this.employee = employee;
    }

    public long getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(long totalEmployees) {
        this.totalEmployees = totalEmployees;
    }
}

