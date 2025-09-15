package com.example.exercise.demo.security;

import com.example.exercise.demo.dto.respMsgs.ResponseMsg;
import com.example.exercise.demo.enums.ResponseStatusEnums;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    // when user tries to access a resource without ADMIN permissions
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        ResponseMsg resp = new ResponseMsg();
        resp.setStatus(ResponseStatusEnums.UNAUTHORIZED.getStatus());
        resp.setMessage(accessDeniedException.getMessage());

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(resp));
    }

}
