package com.csye6225.cloudwebapp.errorhandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class ParamInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!request.getParameterMap().isEmpty() || request.getReader().readLine() != null){
            response.setStatus(400);
            return false;
        }
        return true;
    }
}
