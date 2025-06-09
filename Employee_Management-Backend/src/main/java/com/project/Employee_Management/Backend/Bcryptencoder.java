package com.project.Employee_Management.Backend;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Bcryptencoder {
    public static void main(String[] args)
    {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        String password = "password123";
        System.out.println(encoder.encode(password));
    }


}
