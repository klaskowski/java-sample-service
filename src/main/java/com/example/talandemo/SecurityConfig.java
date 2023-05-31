package com.example.talandemo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .authorizeHttpRequests((requests) -> requests
            .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
            .requestMatchers(HttpMethod.GET, "/", "/v3/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/items").authenticated()
            .requestMatchers(HttpMethod.GET, "/items").authenticated()
            .requestMatchers(HttpMethod.PUT, "/items/{id}").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/items/{id}").authenticated()
            .anyRequest().denyAll())
        .httpBasic();
    return http.build();
  }
}
