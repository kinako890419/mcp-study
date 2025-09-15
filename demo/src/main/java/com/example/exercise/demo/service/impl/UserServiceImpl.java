package com.example.exercise.demo.service.impl;

import com.example.exercise.demo.dto.EditUserInfoReq;
import com.example.exercise.demo.dto.respMsgs.ResponseMsg;
import com.example.exercise.demo.dto.respMsgs.UserProfileResp;
import com.example.exercise.demo.entity.UsersEntity;
import com.example.exercise.demo.enums.ResponseStatusEnums;
import com.example.exercise.demo.enums.UserRolesEnums;
import com.example.exercise.demo.exception.DataNotFoundException;
import com.example.exercise.demo.exception.DeleteFailException;
import com.example.exercise.demo.exception.DuplicatedDataException;
import com.example.exercise.demo.exception.InsertionFailException;
import com.example.exercise.demo.repository.ProjectRepository;
import com.example.exercise.demo.repository.UserRepository;
import com.example.exercise.demo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ProjectRepository projectRepo;


    @Override
    public List<UserProfileResp> getAllUsers(String isDeleted, String userRole) {

        if (StringUtils.equalsIgnoreCase(isDeleted, "true") &&
                StringUtils.equals(userRole, UserRolesEnums.ADMIN.getRole())) {

            List<UsersEntity> deleted =  userRepo.findDeletedUsers();
            return returnUserInfoListResp(deleted);

        }

        List<UsersEntity> users = userRepo.findAllByOrderByCreatedAtAsc();

        // normal users can view all users to invite them to projects but not ADMIN
        if (!StringUtils.equals(userRole, UserRolesEnums.ADMIN.getRole())) {
            users.removeIf(user ->
                    StringUtils.equals(user.getRole(), UserRolesEnums.ADMIN.getRole()));
        }

        return returnUserInfoListResp(users);

    }

    @Override
    public UserProfileResp getUserProfile(Integer userId, String loginUserRole) {

        UsersEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found with ID: " + userId));

        // normal users cannot access ADMIN user profile
        if (!StringUtils.equals(loginUserRole, UserRolesEnums.ADMIN.getRole())
                && StringUtils.equals(user.getRole(), UserRolesEnums.ADMIN.getRole())) {
            throw new DataNotFoundException("User not found with ID: " + userId);
        }

        UserProfileResp resp = new UserProfileResp();

        resp.setId(user.getId());
        resp.setName(user.getUsername());
        resp.setEmail(user.getEmail());
        resp.setRole(user.getRole());
        resp.setCreatedAt(user.getCreatedAt());
        resp.setUpdatedAt(user.getUpdatedAt());

        return resp;

    }

    @Override
    @Transactional
    public ResponseMsg deleteUser(Integer userId) {

        // userRepo.findById and user not deleted
        UsersEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found with ID: " + userId));

        if (StringUtils.equals(user.getRole(), UserRolesEnums.ADMIN.getRole())) {
            throw new DeleteFailException("ADMIN User cannot be deleted");
        }

//        // cannot delete user if the user is the creator of any projects
//        if (projectRepo.existsByCreatorId(userId)) {
//            throw new DeleteFailException("User cannot be deleted because they own projects");
//        }

        // Mark the user as deleted
        userRepo.delete(user);

        return createSuccessResponse("User deleted successfully");

    }

    @Override
    @Transactional
    public ResponseMsg editUser(Integer userId, EditUserInfoReq req, Integer loginUserId, String userRole) {

        UsersEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User to edit is not founded with ID: " + userId));

        if (Objects.nonNull(req.getIsAdmin()) && req.getIsAdmin()) {
            // logged-in user not ADMIN or user that will be edited is ADMIN
            if (!StringUtils.equals(userRole, UserRolesEnums.ADMIN.getRole()) ||
                    StringUtils.equals(user.getRole(), UserRolesEnums.ADMIN.getRole())) {
                throw new InsertionFailException("Cannot edit user role");
            }
            user.setRole(UserRolesEnums.ADMIN.getRole());
            return createSuccessResponse("User upgraded to ADMIN successfully");
        }

        if (!userId.equals(loginUserId)) {
            throw new InsertionFailException("Username can only be changed by the user themselves");
        }

        if (StringUtils.isNotBlank(req.getUsername())) {
            user.setUsername(req.getUsername());
        }

        if (StringUtils.isNotBlank(req.getEmail())) {
            // Check if the email already exists and is different from the current email
            if (userRepo.existsByEmail(req.getEmail()) &&
                    !StringUtils.equals(req.getEmail(), user.getEmail())) {
                throw new DuplicatedDataException("Email already exists");
            }

            user.setEmail(req.getEmail());
        }

        return createSuccessResponse("User updated successfully");
    }

    private List<UserProfileResp> returnUserInfoListResp (List<UsersEntity> users) {

        List<UserProfileResp> respList = new ArrayList<>();

        for (UsersEntity user : users) {

            UserProfileResp resp = new UserProfileResp();

            resp.setId(user.getId());
            resp.setName(user.getUsername());
            resp.setEmail(user.getEmail());
            resp.setRole(user.getRole());
            resp.setCreatedAt(user.getCreatedAt());
            resp.setUpdatedAt(user.getUpdatedAt());

            respList.add(resp);
        }

        return respList;

    }

    private ResponseMsg createSuccessResponse(String message) {
        ResponseMsg resp = new ResponseMsg();
        resp.setStatus(ResponseStatusEnums.SUCCESS.getStatus());
        resp.setMessage(message);
        return resp;
    }

}
