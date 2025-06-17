package com.project.Employee_Management.Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class OTPService {

    @Autowired
    JavaMailSender mailSender;


    public boolean verifyOtp(String otpStored, String otpEmail) {


        return (otpEmail.equals(otpStored));
    }

    public String GenerateOTP(String email) {
        String OtpNum = String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("dummy.address.901@gmail.com");
        msg.setTo(email);
        msg.setSubject("OTP - password Reset");
        msg.setText("OTP Generated for Password Reset :  "+ OtpNum);
        mailSender.send(msg);

        return OtpNum;


    }
}
