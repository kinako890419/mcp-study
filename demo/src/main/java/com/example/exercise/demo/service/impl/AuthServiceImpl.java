package com.example.exercise.demo.service.impl;

import com.example.exercise.demo.dto.respMsgs.LoginResp;
import com.example.exercise.demo.dto.respMsgs.ResponseMsg;
import com.example.exercise.demo.dto.respMsgs.UserProfileResp;
import com.example.exercise.demo.entity.UsersEntity;
import com.example.exercise.demo.enums.ResponseStatusEnums;
import com.example.exercise.demo.enums.UserRolesEnums;
import com.example.exercise.demo.exception.DuplicatedDataException;
import com.example.exercise.demo.repository.UserRepository;
import com.example.exercise.demo.security.UserLoginReqDto;
import com.example.exercise.demo.security.UserRegisterReqDto;
import com.example.exercise.demo.security.jwt.JwtService;
import com.example.exercise.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ResponseMsg userRegister(UserRegisterReqDto req) {

        // TODO: Users cannot register again id their account already exists or is deleted

        if (userRepo.existsByEmail(req.getEmail())) {
            throw new DuplicatedDataException("User account already exists");
        }

        UsersEntity user = new UsersEntity();
        user.setUsername(req.getUsername().trim()); // Remove 開頭結尾的 spaces
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword())); // Password encoding
        user.setRole(UserRolesEnums.USER.getRole());
        user.setIsDeleted(false);

        userRepo.save(user);


        ResponseMsg response = new ResponseMsg();

        response.setStatus(ResponseStatusEnums.SUCCESS.getStatus());
        response.setMessage("User registered successfully");

        return response;
    }

    @Override
    public LoginResp userLogin(UserLoginReqDto req) {

        String email = req.getUserMail();

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, req.getUserPassword())
        );

        UsersEntity user = userRepo.findByEmail(email);

        String token = jwtService.generateToken(user.getId(), email, authentication.getAuthorities());

        UserProfileResp userProf = new UserProfileResp();
        userProf.setId(user.getId());
        userProf.setName(user.getUsername());
        userProf.setEmail(user.getEmail());
        userProf.setRole(user.getRole());
        userProf.setCreatedAt(user.getCreatedAt());
        userProf.setUpdatedAt(user.getUpdatedAt());

        LoginResp resp = new LoginResp();
        resp.setToken(token);
        resp.setUserProfile(userProf);

        return resp;

    }

}
