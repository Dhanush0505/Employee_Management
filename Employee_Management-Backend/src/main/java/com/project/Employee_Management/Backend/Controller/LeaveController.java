package com.project.Employee_Management.Backend.Controller;

import com.project.Employee_Management.Backend.Model.Leave;
import com.project.Employee_Management.Backend.Service.LeaveService;
import com.project.Employee_Management.Backend.dto.leaverequestdto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    // Employee applies for leave
    @PostMapping("/apply")
    public ResponseEntity<Leave> applyLeave(@Valid @RequestBody leaverequestdto leaveRequestDto) {
        Leave leave = leaveService.applyLeave(leaveRequestDto);
        return ResponseEntity.ok(leave);
    }

    // Admin approves or rejects leave (Admin only)
    @PutMapping("/{leaveId}/decision")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Leave> decideLeave(
            @PathVariable Long leaveId,
            @RequestParam boolean approve) {
        Leave leave = leaveService.processLeaveDecision(leaveId, approve);
        return ResponseEntity.ok(leave);
    }

    // Admin view all leaves
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Leave>> getAllLeaves() {
        List<Leave> leaves = leaveService.getAllLeaves();
        return ResponseEntity.ok(leaves);
    }

    // Employee views own leave applications
    @GetMapping("/my")
    public ResponseEntity<List<Leave>> getMyLeaves() {
        List<Leave> leaves = leaveService.getLeavesForCurrentUser();
        return ResponseEntity.ok(leaves);
    }

    @GetMapping("/my-leaves")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<Leave>> getMyLeaves(Authentication authentication) {
        String username = authentication.getName();
        List<Leave> myLeaves = leaveService.getLeavesByUsername(username);
        return ResponseEntity.ok(myLeaves);
    }

}

