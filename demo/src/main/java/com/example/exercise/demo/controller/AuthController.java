package com.example.exercise.demo.controller;

import com.example.exercise.demo.dto.respMsgs.LoginResp;
import com.example.exercise.demo.dto.respMsgs.ResponseMsg;
import com.example.exercise.demo.security.UserLoginReqDto;
import com.example.exercise.demo.security.UserRegisterReqDto;
import com.example.exercise.demo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "1. User Authentication", description = "Authentication APIs")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "User login",
            description = """
                    - User login with email and password, return JWT token and user profile
                    - token expires in 24 hr
                    """)
    public LoginResp login(@Valid @RequestBody UserLoginReqDto req) {
        return authService.userLogin(req);
    }

    @PostMapping("/register")
    @Operation(summary = "User registration",
            description = """
                    - User registration with email, password, and name.
                    - An email can only register one account.
                    - User ROLE = USER (spring security role) after registered successfully by default.
                    
                    restrictions:
                    1. username: 3~20 characters
                    2. password: 3~20 characters
                    3. email: valid email format, less than 50 characters
                    """)
    public ResponseMsg register(@Valid @RequestBody UserRegisterReqDto req) {
        return authService.userRegister(req);
    }
}
