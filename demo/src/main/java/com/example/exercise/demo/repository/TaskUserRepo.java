package com.example.exercise.demo.repository;

import com.example.exercise.demo.entity.TaskUsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskUserRepo extends JpaRepository<TaskUsersEntity, Integer> {

    Boolean existsByUserIdAndTaskId(Integer userId, Integer taskId);

    List<TaskUsersEntity> findByTaskId(Integer id);

    Optional<TaskUsersEntity> findByUserIdAndTaskId(Integer userId, Integer taskId);

}
