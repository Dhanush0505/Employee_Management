package com.project.Employee_Management.Backend.Model;

import com.project.Employee_Management.Backend.Model.Enum.LeaveStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "employee_leave")  // changed table name here
@Data
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")  // foreign key column name
    private User user;

    private String username;
    private LocalDate startDate;  // changed to camelCase
    private LocalDate endDate;    // changed to camelCase
    private String reason;

    @Enumerated(EnumType.STRING)
    private LeaveStatus leaveStatus;


}
