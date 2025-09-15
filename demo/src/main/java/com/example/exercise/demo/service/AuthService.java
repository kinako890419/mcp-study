package com.example.exercise.demo.service;

import com.example.exercise.demo.dto.respMsgs.LoginResp;
import com.example.exercise.demo.dto.respMsgs.ResponseMsg;
import com.example.exercise.demo.security.UserLoginReqDto;
import com.example.exercise.demo.security.UserRegisterReqDto;
import org.springframework.stereotype.Service;


@Service
public interface AuthService {

    ResponseMsg userRegister(UserRegisterReqDto req);
    LoginResp userLogin(UserLoginReqDto req);

}
