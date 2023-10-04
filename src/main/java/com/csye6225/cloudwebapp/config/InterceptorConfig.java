package com.csye6225.cloudwebapp.config;

import com.csye6225.cloudwebapp.errorhandler.ParamInterceptor;
import com.csye6225.cloudwebapp.errorhandler.UserRoleInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public UserRoleInterceptor userRoleInterceptor(){
        return new UserRoleInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ParamInterceptor())
                .addPathPatterns("/healthz","/v1/**");
        //.excludePathPatterns("");
        registry.addInterceptor(userRoleInterceptor())
                .addPathPatterns("/v1/assignments/{assignmentId}")
                .excludePathPatterns("/v1/assignments");
    }
}
