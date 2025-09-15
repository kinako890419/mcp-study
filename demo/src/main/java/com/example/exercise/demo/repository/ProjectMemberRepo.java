package com.example.exercise.demo.repository;

import com.example.exercise.demo.entity.ProjectMembersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMemberRepo extends JpaRepository<ProjectMembersEntity, Integer> {

    Boolean existsByProjectIdAndUserId(Integer id, Integer userId);

    Optional<ProjectMembersEntity> findByUserIdAndProjectId(Integer userId, Integer id);

    List<ProjectMembersEntity> findByProjectIdOrderByCreatedAtAsc(Integer projectId);
}
