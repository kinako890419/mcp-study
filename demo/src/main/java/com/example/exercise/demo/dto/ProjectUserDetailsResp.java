package com.example.exercise.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ProjectUserDetailsResp {

    @JsonProperty("user_id")
    private Integer id;

    @JsonProperty("user_email")
    private String email;

    @JsonProperty("user_name")
    private String name;

    @JsonProperty("user_project_role")
    private String role;

    @JsonProperty("invited_by")
    private String invitedBy;

    @JsonProperty("created_at")
    private Timestamp createdAt;

    @JsonProperty("updated_at")
    private Timestamp updatedAt;
}
