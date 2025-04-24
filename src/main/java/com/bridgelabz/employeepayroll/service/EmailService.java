package com.bridgelabz.employeepayroll.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("gargdivyansh7599@gmail.com");
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
        mailSender.send(mailMessage);
        System.out.println("Email Sent to the user!");
    }

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("gargdivyansh7599@gmail.com");
        mailMessage.setTo(to);
        mailMessage.setSubject("Your OTP for Password Reset!");
        mailMessage.setText("Your OTP for resetting your password is: " + otp + "\n\nThis OTP will expire in 5 minutes.");
        mailSender.send(mailMessage);
        System.out.println("OTP email sent to: " + to);
    }
}
