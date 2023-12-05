package com.csye6225.cloudwebapp.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;


@Configuration
public class SecurityConfiguration {
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager jdbcUserDetailsManager =  new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager
                .setUsersByUsernameQuery("SELECT email, password, enabled from users where email=?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select email, role from users where email=?");
        return jdbcUserDetailsManager;
    }

    @Bean
     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.exceptionHandling((exception)-> exception.authenticationEntryPoint(authenticationEntryPoint));
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/healthz", "/", "/swagger-ui/**",
                        "/v3/api-docs/**").permitAll());
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(HttpMethod.GET, "/v2/assignments/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE,"/v2/assignments/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/v2/assignments/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/v2/assignments").authenticated()
                        .requestMatchers(HttpMethod.POST, "/v2/assignments/{id}/submission").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/v2/assignments/**").permitAll());

       // http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.httpBasic(Customizer.withDefaults());
        http.csrf(csrf -> csrf.disable());
        return http.build();
    }

}
