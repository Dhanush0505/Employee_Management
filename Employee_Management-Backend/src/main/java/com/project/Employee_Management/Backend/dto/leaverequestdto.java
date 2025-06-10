package com.project.Employee_Management.Backend.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class leaverequestdto {
    @NotNull(message = "Start date is required")
    private LocalDate  startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private String reason;

    // Getters and setters
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
