package com.example.exercise.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AssignTaskMemberReq {

    /** no role settings in a task */

    @JsonProperty("user_id")
    private Integer userId;

}
