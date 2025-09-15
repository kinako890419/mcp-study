package com.example.exercise.demo.repository;

import com.example.exercise.demo.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UsersEntity, Integer> {
    UsersEntity findByEmail(String email);

    Boolean existsByEmail(String newUserMail);

    @Query(value = "SELECT * FROM users WHERE is_deleted = true", nativeQuery = true)
    List<UsersEntity> findDeletedUsers();

    List<UsersEntity> findAllByOrderByCreatedAtAsc();
}
