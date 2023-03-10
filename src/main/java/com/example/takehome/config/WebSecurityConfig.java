package com.example.takehome.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private CustomBasicAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public UserDetailsService users() {
        UserDetails user = User.builder().username("user")
                .password(passwordEncoder().encode("user")).roles("USER")
                .build();
        UserDetails admin = User.builder().username("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("USER", "ADMIN").build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.anonymous().principal("guest").authorities("ROLE_GUEST").and()
                .authorizeHttpRequests().requestMatchers("/country/get")
                .hasAnyAuthority("ROLE_GUEST", "ROLE_USER").and().httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint);
        return http.build();
    }

}
