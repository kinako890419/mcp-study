package com.example.exercise.demo.repository;

import com.example.exercise.demo.entity.TaskAttachmentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskAttachmentsRepo extends JpaRepository<TaskAttachmentsEntity, Integer> {
    List<TaskAttachmentsEntity> findByTaskId(Integer taskId);

    Optional<TaskAttachmentsEntity> findByIdAndTaskId(Integer attachId, Integer taskId);
}
