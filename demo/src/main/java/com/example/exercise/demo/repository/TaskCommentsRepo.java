package com.example.exercise.demo.repository;

import com.example.exercise.demo.entity.TaskCommentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskCommentsRepo extends JpaRepository<TaskCommentsEntity, Integer> {
    List<TaskCommentsEntity> findByTaskIdOrderByIdAsc(Integer taskId);

    Optional<TaskCommentsEntity> findByIdAndTaskId(Integer commentId, Integer taskId);
}
