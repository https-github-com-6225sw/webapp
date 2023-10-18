package com.csye6225.cloudwebapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;


@Configuration
public class SecurityConfiguration {
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
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/healthz", "/swagger-ui/**",
                        "/v3/api-docs/**").permitAll());
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(HttpMethod.GET, "/v1/assignments/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE,"/v1/assignments/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/v1/assignments/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/v1/assignments").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/v1/assignments/**").permitAll());


       // http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.httpBasic(Customizer.withDefaults());
        http.csrf(csrf -> csrf.disable());
        return http.build();
    }
}
