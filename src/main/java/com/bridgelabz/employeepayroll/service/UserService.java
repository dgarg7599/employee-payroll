package com.bridgelabz.employeepayroll.service;

import com.bridgelabz.employeepayroll.dto.*;
import com.bridgelabz.employeepayroll.exceptions.EmployeePayrollException;
import com.bridgelabz.employeepayroll.model.User;
import com.bridgelabz.employeepayroll.repository.UserRepository;
import com.bridgelabz.employeepayroll.util.JwtUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpService otpService;

    @Override
    public ResponseDTO registerUser(RegisterDTO registerDTO) {
        log.info("Registering user with email: {}", registerDTO.getEmail());

        // Check if email already registered or not
        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            log.warn("Registration failed: Email already registered - {}", registerDTO.getEmail());
            throw new EmployeePayrollException("Email already registered");
        }

        // Encode password
        String encodedPassword = bCryptPasswordEncoder.encode(registerDTO.getPassword());
        log.debug("Encoded password generated");

        User user = new User();
        user.setFullName(registerDTO.getFullName());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(encodedPassword);

        userRepository.save(user);
        log.info("User saved successfully with email: {}", user.getEmail());

        emailService.sendEmail(user.getEmail(), "Registered in Employee Payroll App", "Thank You! You are successfully registered in Employee Payroll App!");
        RegisterResponseDTO registerResponse = new RegisterResponseDTO(user.getFullName(), user.getEmail());
        log.info("Registration successful for user: {}", registerResponse.getEmail());
        return new ResponseDTO("User Registered Successfully", registerResponse);
    }

    @Override
    public ResponseDTO loginUser(LoginDTO loginDTO) {
        log.info("Attempting login for email: {}", loginDTO.getEmail());

        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed: Invalid email - {}", loginDTO.getEmail());
                    return new EmployeePayrollException("Invalid Email or password");
                });

        if (!bCryptPasswordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            log.warn("Login failed: Incorrect password for email - {}", loginDTO.getEmail());
            throw new EmployeePayrollException("Invalid Email or password");
        }

        String token = jwtUtility.generateToken(user.getEmail());
        log.debug("JWT token generated for user: {}", user.getEmail());

        emailService.sendEmail(user.getEmail(), "Login Successful", "Hi " + user.getFullName() + ",\n\n" + "You have successfully logged in to Employee Payroll App. \n\n" + "Your JWT Token is:\n\n" + token + "\n\n");
        log.info("Login successful and token saved for user: {}", user.getEmail());

        LoginResponseDTO loginResponse = new LoginResponseDTO(user.getFullName(), user.getEmail(), token);
        return new ResponseDTO("Login Successful", loginResponse);
    }

    @Override
    public ResponseDTO forgotPassword(RegisterDTO request) {
        String email = request.getEmail();

        log.info("EMail: " + email);

        User user = userRepository.findByEmail(email).orElseThrow(()-> new EmployeePayrollException("User not found with this email: " + email));

        String otp = otpService.generateOtp(email);
        emailService.sendOtpEmail(email, otp);

        return new ResponseDTO("OTP sent to your email. It will expire in 5 minutes.", request.getEmail());
    }

    @Override
    public ResponseDTO resetPassword(ResetPasswordRequestDTO request) {

        log.info("Reset Password for email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EmployeePayrollException("User not found with email: " + request.getEmail()));

        boolean isValidOtp = otpService.validateOtp(user.getEmail(),  request.getOtp());

        if(!isValidOtp) {
            log.warn("Invalid or expired OTP for email: {}", request.getEmail());
            throw new EmployeePayrollException("Invalid or Expired OTP");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);

        log.info("Password reset successful for email: {}", user.getEmail());
        return new ResponseDTO("Password reset successfully", user.getEmail());
    }

    @Override
    public ResponseDTO changePassword(ChangePasswordDTO request, String token) {
        log.info("Changing Password...");
        String email = jwtUtility.extractEmail(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmployeePayrollException("User not found"));

        if (!bCryptPasswordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            log.warn("Old password is incorrect");
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        Map<String, Object> data = new HashMap<>();
        data.put("email", email);
        data.put("message", "Password updated successfully");
        data.put("timestamp", LocalDateTime.now());

        log.info("Password changed successfully for email: {}", user.getEmail());
        return new ResponseDTO("Password changed successfully", data);
    }
}
