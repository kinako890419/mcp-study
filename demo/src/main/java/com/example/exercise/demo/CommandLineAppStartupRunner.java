package com.example.exercise.demo;

import com.example.exercise.demo.entity.StatusEntity;
import com.example.exercise.demo.entity.UsersEntity;
import com.example.exercise.demo.enums.ProjectTaskEnums;
import com.example.exercise.demo.enums.UserRolesEnums;
import com.example.exercise.demo.repository.StatusRepository;
import com.example.exercise.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StatusRepository statusRepository;

    @Override
    public void run(String[] args){
        if (!userRepository.existsByEmail("admin0@test.com")) {
            UsersEntity admin = new UsersEntity();
            admin.setUsername("admin");
            admin.setEmail("admin0@test.com");
            admin.setPassword(passwordEncoder.encode("123"));
            admin.setRole(UserRolesEnums.ADMIN.getRole());
            admin.setIsDeleted(false);
            userRepository.save(admin);
        }
        for (ProjectTaskEnums e : ProjectTaskEnums.values()) {
            if (statusRepository.findById(e.getStatusId()).isEmpty()
                    && statusRepository.findByName(e.getStatusName()).isEmpty()) {
                StatusEntity status = new StatusEntity();
                status.setId(e.getStatusId());
                status.setName(e.getStatusName());
                statusRepository.save(status);
            }
        }

    }
}
