package com.example.faultinjectionmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig{
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //配置HTTP安全性，所有请求都需要认证
        //启用表单登录作为认证机制
        http
                .authorizeHttpRequests(authorizeRequests->
                        authorizeRequests
                                .anyRequest().authenticated()
                        ).formLogin(Customizer.withDefaults());
        return http.build();
    }
}
