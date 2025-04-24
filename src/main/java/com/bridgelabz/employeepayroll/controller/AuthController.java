package com.bridgelabz.employeepayroll.controller;

import com.bridgelabz.employeepayroll.dto.*;
import com.bridgelabz.employeepayroll.model.User;
import com.bridgelabz.employeepayroll.service.IUserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUserService userService;


    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerUser(@Valid @RequestBody RegisterDTO registerDTO) {
        System.out.println("Register API hit");
        log.info("Registering User: {}", registerDTO.getEmail());
        ResponseDTO responseDTO = userService.registerUser(registerDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        System.out.println("Login API hit");
        log.info("Login User: {}", loginDTO.getEmail());
        ResponseDTO responseDTO = userService.loginUser(loginDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/forgot")
    public ResponseEntity<ResponseDTO> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO request){
        log.info("Forgot Password request for email: {}", request.getEmail());
        ResponseDTO responseDTO = userService.forgotPassword(request);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/reset")
    public ResponseEntity<ResponseDTO> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO request){
        log.info("Reset Password request for email: {}", request.getEmail());
        ResponseDTO responseDTO = userService.resetPassword(request);
        return ResponseEntity.ok(responseDTO);
    }
}
