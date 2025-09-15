package com.example.exercise.demo.config;

import com.example.exercise.demo.security.CustomAccessDeniedHandler;
import com.example.exercise.demo.security.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sessionManage -> sessionManage.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.DELETE, "/users/{id}").hasRole("ADMIN")

                        // allow spring doc API documentation without authentication
                        .requestMatchers("/api-docs", "/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                        // user registration endpoint
                        .requestMatchers("/auth/register", "/auth/login").permitAll()

                        .anyRequest().authenticated()
                )

                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(new CustomAccessDeniedHandler()) // 403 Forbidden
                )

        ;

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
//                                                         PasswordEncoder passwordEncoder) {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
//        provider.setPasswordEncoder(passwordEncoder);
//
//        return provider;
//    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


//    @Bean
//    UserDetailsService testUsers() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//
//        // Create ADMIN account
//        UserDetails admin = User.builder()
//                .username("admin@example.com")
//                .password("{noop}admin123")
//                .authorities("ADMIN")
//                .build();
//
//        // Create USER account
//        UserDetails user = User.builder()
//                .username("user@example.com")
//                .password("{noop}user123")
//                .authorities("USER")
//                .build();
//
//        manager.createUser(admin);
//        manager.createUser(user);
//
//        return manager;
//    }
}

