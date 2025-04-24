package com.bridgelabz.employeepayroll.service;

import com.bridgelabz.employeepayroll.dto.*;

public interface IUserService {

    ResponseDTO registerUser(RegisterDTO registerDTO);
    ResponseDTO loginUser(LoginDTO loginDTO);
    ResponseDTO forgotPassword(RegisterDTO request);
    ResponseDTO resetPassword(ResetPasswordRequestDTO request);
    ResponseDTO changePassword(ChangePasswordDTO request, String token);
}
