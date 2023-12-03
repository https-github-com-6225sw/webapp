package com.csye6225.cloudwebapp.config;

import com.csye6225.cloudwebapp.errorhandler.BodyInterceptor;
import com.csye6225.cloudwebapp.errorhandler.ParamInterceptor;
//import com.csye6225.cloudwebapp.errorhandler.UserRoleInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ParamInterceptor())
                .addPathPatterns("/healthz", "/v1/**", "/v1/**/**");
        //.excludePathPatterns("");
      
        registry.addInterceptor(new BodyInterceptor())
                .addPathPatterns("/healthz");



    }
}
