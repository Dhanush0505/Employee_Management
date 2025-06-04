package com.project.Employee_Management.Backend.Service;

import com.project.Employee_Management.Backend.Model.Enum.LeaveStatus;
import com.project.Employee_Management.Backend.Model.Leave;
import com.project.Employee_Management.Backend.Model.User;
import com.project.Employee_Management.Backend.Repository.LeaveRepository;
import com.project.Employee_Management.Backend.dto.leaverequestdto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveService {

    private final LeaveRepository leaveRepository;
    private final UserService userService;

    public LeaveService(LeaveRepository leaveRepository, UserService userService) {
        this.leaveRepository = leaveRepository;
        this.userService = userService;
    }

    // Employee applies for leave
    public Leave applyLeave(leaverequestdto dto) {
        User user = userService.getCurrentUser();

        Leave leave = new Leave();
        leave.setUsername(user.getUsername());
        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setReason(dto.getReason());
        leave.setLeaveStatus(LeaveStatus.PENDING);

        return leaveRepository.save(leave);
    }

    // Admin approves or rejects leave
    public Leave processLeaveDecision(Long leaveId, boolean approve) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new EntityNotFoundException("Leave not found"));

        if (approve) {
            leave.setLeaveStatus(LeaveStatus.APPROVED);
        } else {
            leave.setLeaveStatus(LeaveStatus.REJECTED);
        }
        return leaveRepository.save(leave);
    }

    // Get all leaves (admin)
    public List<Leave> getAllLeaves() {
        return leaveRepository.findAll();
    }

    // Get leaves for current user (employee)
    public List<Leave> getLeavesForCurrentUser() {
        User user = userService.getCurrentUser();
        return leaveRepository.findByUsername(user.getUsername());
    }
}