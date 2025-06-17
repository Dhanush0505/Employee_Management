package com.project.Employee_Management.Backend.Model;

import com.project.Employee_Management.Backend.Model.Enum.role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @UniqueElements
    @NotNull
    private String email;
    private Long mobilenumber;

    @Enumerated(EnumType.STRING)
    private role Role;

    private LocalDate dateofjoining;
    private String department;


}
