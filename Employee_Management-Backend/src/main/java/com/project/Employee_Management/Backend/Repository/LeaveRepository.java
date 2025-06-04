package com.project.Employee_Management.Backend.Repository;

import com.project.Employee_Management.Backend.Model.Leave;
import com.project.Employee_Management.Backend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {

    // Find leaves by the user (employee)
    List<Leave> findByUsername(String username);

}
