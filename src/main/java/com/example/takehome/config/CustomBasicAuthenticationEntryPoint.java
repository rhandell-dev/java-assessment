package com.example.takehome.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.takehome.exceptions.CustomErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomBasicAuthenticationEntryPoint
        extends BasicAuthenticationEntryPoint {

    @Autowired
    private ObjectMapper jsonMapper;

    @Override
    public void commence(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException authEx)
            throws IOException {
        CustomErrorResponse errRes = new CustomErrorResponse(
                HttpStatus.UNAUTHORIZED.value(), authEx.getMessage());
        response.getWriter().write(jsonMapper.writeValueAsString(errRes));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("test");
        super.afterPropertiesSet();
    }

}
