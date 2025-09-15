package com.example.exercise.demo.dto.respMsgs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseMsg {

    /** 成功或失敗 */
    @JsonProperty("status_type")
    private String status;

    /** 成功或錯誤訊息 */
    @JsonProperty("response_message")
    private String message;
}
