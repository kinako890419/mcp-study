package com.example.exercise.demo.service;

import com.example.exercise.demo.dto.EditUserInfoReq;
import com.example.exercise.demo.dto.respMsgs.ResponseMsg;
import com.example.exercise.demo.dto.respMsgs.UserProfileResp;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    /** ADMIN delete users (set is deleted to true, 停用) */
    ResponseMsg deleteUser(Integer userId);

    UserProfileResp getUserProfile(Integer id, String userRole);

    List<UserProfileResp> getAllUsers(String isDeleted, String userRole);

    ResponseMsg editUser(Integer userId, EditUserInfoReq req, Integer loginUserId, String userRole);
}
