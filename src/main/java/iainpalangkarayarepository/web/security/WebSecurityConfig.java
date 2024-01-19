package iainpalangkarayarepository.web.security;

import iainpalangkarayarepository.web.model.Permission;
import iainpalangkarayarepository.web.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    
    private final AuthenticationProvider authenticationProvider;
    
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/login", "/register")
                        .permitAll()
                        
                        .requestMatchers("/users/**").hasAnyRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/users/**").hasAnyAuthority(Permission.ADMIN_READ.name())
                        .requestMatchers(HttpMethod.POST, "/users/**").hasAnyAuthority(Permission.ADMIN_WRITE.name())
                        .requestMatchers(HttpMethod.PUT, "/users/**").hasAnyAuthority(Permission.ADMIN_UPDATE.name())
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasAnyAuthority(Permission.ADMIN_DELETE.name())
                        
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                        httpSecurityExceptionHandlingConfigurer
                                .accessDeniedHandler(customAccessDeniedHandler)
                                .authenticationEntryPoint(customAuthenticationFailureHandler)
                );

        return http.build();
    }

}
