package com.csye6225.cloudwebapp.errorhandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

public class PatchInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        System.out.println("method - " + method);
        if (method.equals("PATCH")){
            response.sendError(HttpStatus.METHOD_NOT_ALLOWED.value());
            return false;
        }
        return true;
    }
}
