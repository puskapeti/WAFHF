package com.example.wafhfbackend.configurations

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class WebSecurityConfig {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeRequests()
            .mvcMatchers("/", "/home", "/register", "/registerUser", "/admin").permitAll()
            .mvcMatchers("/recipes/**").permitAll()
            .mvcMatchers("/recipes/edit/**", "/recipes/delete/**", "/recipes/create/**").authenticated()
            .mvcMatchers("/css/**", "/images/**").permitAll()
            .mvcMatchers("/admin").hasAnyRole("ADMIN")
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/", true)
            .permitAll()
            .and()
            .logout()
            .logoutSuccessUrl("/login")
            .permitAll()
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}