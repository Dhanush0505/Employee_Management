package com.project.Employee_Management.Backend.dto;

import com.project.Employee_Management.Backend.Model.Enum.LeaveStatus;

public class LeaveDTO {
    private Long id;
    private String startDate;
    private String endDate;
    private String reason;
    private LeaveStatus leaveStatus;
}

