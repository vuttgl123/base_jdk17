package com.base.configuration.config;

import com.base.configuration.filter.JwtAuthenticationFilter;
import com.base.provider.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtTokenProvider tokenProvider,
                          UserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = new JwtAuthenticationFilter(tokenProvider, userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET,  PermitPaths.GET).permitAll()
                        .requestMatchers(HttpMethod.POST, PermitPaths.POST).permitAll()
                        .requestMatchers(HttpMethod.PUT,  PermitPaths.PUT).permitAll()
                        .requestMatchers(HttpMethod.PATCH,PermitPaths.PATCH).permitAll()
                        .requestMatchers(HttpMethod.DELETE,PermitPaths.DELETE).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    public static final class PermitPaths {
        private PermitPaths(){}

        public static final String[] GET = {
                "/swagger*/**", "/v3/api-docs/**", "/actuator/health/**", "/actuator/info",
                "/ping/**", "/public/**", "/swagger-ui/**", "/swagger-ui.html"
        };
        public static final String[] POST  = {"/auth/**", "/public/**", "/internal/**"};
        public static final String[] PUT   = {"/public/**", "/internal/**"};
        public static final String[] PATCH = {"/public/**", "/internal/**"};
        public static final String[] DELETE= {"/public/**", "/internal/**"};
    }
}
