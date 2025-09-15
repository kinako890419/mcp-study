package com.example.exercise.demo.dto.respMsgs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginResp {

    @JsonProperty("token")
    private String token;

    @JsonProperty("user")
    private UserProfileResp userProfile;

}
