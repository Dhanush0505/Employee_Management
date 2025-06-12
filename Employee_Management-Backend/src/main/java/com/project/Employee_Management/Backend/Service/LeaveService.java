package com.project.Employee_Management.Backend.Service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.project.Employee_Management.Backend.Model.Enum.LeaveStatus;
import com.project.Employee_Management.Backend.Model.Leave;
import com.project.Employee_Management.Backend.Model.User;
import com.project.Employee_Management.Backend.Repository.LeaveRepository;
import com.project.Employee_Management.Backend.dto.leaverequestdto;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.List;

@Service
public class LeaveService {

    private final LeaveRepository leaveRepository;
    private final UserService userService;
    private final JavaMailSender mailSender;


    public LeaveService(LeaveRepository leaveRepository, UserService userService, JavaMailSender mailSender) throws IOException {
        this.leaveRepository = leaveRepository;
        this.userService = userService;
        this.mailSender = mailSender;
    }

    ClassPathResource resource = new ClassPathResource("templates/EmailHtmlPage.html");
    String htmlTemplate = new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);

    // Employee applies for leave
    public Leave applyLeave(leaverequestdto dto) {
        User user = userService.getCurrentUser();

        Leave leave = new Leave();
        leave.setUsername(user.getUsername());
        leave.setEmail(user.getEmail());
        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setReason(dto.getReason());
        leave.setLeaveStatus(LeaveStatus.PENDING);

        //Email generation here for leave request apply
        try{

//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setFrom("dummy.address.901@gmail.com");
//        msg.setTo(user.getEmail());
//        msg.setSubject("Leave Request from "+ user.getUsername());
//        msg.setText("Leave has been set by "+ user.getUsername() + "\n\nStartDate : " + dto.getStartDate() +
//                "\nEndDate : " + dto.getEndDate() + "\nReason : " + dto.getReason() + "\nStatus : " + leave.getLeaveStatus()) ;
//
//        mailSender.send(msg);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(leave.getEmail());
            helper.setSubject("Leave Request Status Update");
            helper.setFrom("dummy.address.901@gmail.com");

            // Replace placeholders with actual values

            String htmlContent = htmlTemplate
                    .replace("{{username}}", leave.getUsername())
                    .replace("{{leaveStatus}}", leave.getLeaveStatus().toString())
                    .replace("{{startDate}}", leave.getStartDate().toString())
                    .replace("{{endDate}}", leave.getEndDate().toString())
                    .replace("{{reason}}", leave.getReason());

            helper.setText(htmlContent, true); // true = isHtml

            mailSender.send(message);

        }
        catch(Exception e){
            System.err.println("Error sending email: " + e.getMessage());
            e.printStackTrace();  // Optional: to see full stack trace in logs
            throw new RuntimeException("Failed to send leave request email");
        }

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

        System.out.println("--------------------------------------------Email from leave entity :" + leave.getEmail());

        //Email generation after admin Approval or Rejection
        try{

//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setFrom("dummy.address.901@gmail.com");
//        msg.setTo(leave.getEmail());
//        msg.setSubject("Leave Request from "+ leave.getUsername());
//        msg.setText("Leave has been set by "+ leave.getUsername() + "\n\nStartDate : " + leave.getStartDate() +
//                "\nEndDate : " + leave.getEndDate() + "\nReason : " + leave.getReason() + "\nStatus : " + leave.getLeaveStatus()) ;
//
//       mailSender.send(msg);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(leave.getEmail());
            helper.setSubject("Leave Request Status Update");
            helper.setFrom("dummy.address.901@gmail.com");

            // Replace placeholders with actual values

            String htmlContent = htmlTemplate
                    .replace("{{username}}", leave.getUsername())
                    .replace("{{leaveStatus}}", leave.getLeaveStatus().toString())
                    .replace("{{startDate}}", leave.getStartDate().toString())
                    .replace("{{endDate}}", leave.getEndDate().toString())
                    .replace("{{reason}}", leave.getReason());

            helper.setText(htmlContent, true); // true = isHtml

            mailSender.send(message);


        }catch (Exception e){
            System.err.println("Error sending email: " + e.getMessage());
            e.printStackTrace();  // Optional: to see full stack trace in logs
            throw new RuntimeException("Failed to send leave request email");
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

    public long countLeavesByStatus(String status) {
        LeaveStatus leaveStatus = LeaveStatus.valueOf(status.toUpperCase());
        return leaveRepository.countByLeaveStatus(leaveStatus);
    }
    public List<Leave> getLeavesByUsername(String username) {
        return leaveRepository.findByUsername(username);
    }

}