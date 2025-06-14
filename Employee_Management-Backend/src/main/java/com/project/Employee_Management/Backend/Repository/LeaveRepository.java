package com.project.Employee_Management.Backend.Repository;

import com.project.Employee_Management.Backend.Model.Enum.LeaveStatus;
import com.project.Employee_Management.Backend.Model.Leave;
import com.project.Employee_Management.Backend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {

    // Find leaves by the user (employee)
    List<Leave> findByUsername(String username);

    long countByLeaveStatus(LeaveStatus status);

    
}
