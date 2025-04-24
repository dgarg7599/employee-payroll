package com.bridgelabz.employeepayroll.service;

import com.bridgelabz.employeepayroll.dto.LoginDTO;
import com.bridgelabz.employeepayroll.dto.RegisterDTO;
import com.bridgelabz.employeepayroll.dto.ResponseDTO;
import com.bridgelabz.employeepayroll.dto.LoginResponseDTO;
import com.bridgelabz.employeepayroll.dto.RegisterResponseDTO;
import com.bridgelabz.employeepayroll.exceptions.EmployeePayrollException;
import com.bridgelabz.employeepayroll.model.User;
import com.bridgelabz.employeepayroll.repository.UserRepository;
import com.bridgelabz.employeepayroll.util.JwtUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

        user.setToken(token);
        userRepository.save(user);
        emailService.sendEmail(user.getEmail(), "Login Successful", "Hi " + user.getFullName() + ",\n\n" + "You have successfully logged in to Employee Payroll App. \n\n" + "Your JWT Token is:\n\n" + token + "\n\n");
        log.info("Login successful and token saved for user: {}", user.getEmail());

        LoginResponseDTO loginResponse = new LoginResponseDTO(user.getFullName(), user.getEmail(), token);
        return new ResponseDTO("Login Successful", loginResponse);
    }
}
