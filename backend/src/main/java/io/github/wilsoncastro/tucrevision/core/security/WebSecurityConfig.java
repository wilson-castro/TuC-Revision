package io.github.wilsoncastro.tucrevision.core.security;


import io.github.wilsoncastro.tucrevision.core.config.LoggingFilter;
import io.github.wilsoncastro.tucrevision.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserRepository userRepository;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final AuthJwtFilter authTokenFilter;


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final String[] permitAll = {
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui-custom.html",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/swagger-ui/index.html",
            "/api-docs/**"
    };

    private final String[] permitPost = {
            "/api/auth/login",
            "/api/auth/login/",
            "/api/users",
            "/api/users/"
    };

    private final String[] permitGet = {
            "/api/users/exists/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        log.info("FilterChain Configuration - STARTED");

        http
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(exceptionHandlingConfigurer ->
                    exceptionHandlingConfigurer
                            .authenticationEntryPoint(unauthorizedHandler)
            )
            .sessionManagement(httpSecuritySessionManagementConfigurer ->
                    httpSecuritySessionManagementConfigurer
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests((authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry
                        .requestMatchers(HttpMethod.POST, permitPost).permitAll()
                        .requestMatchers(HttpMethod.GET, permitGet).permitAll()
                        .requestMatchers(permitAll).permitAll()
            ))
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                        .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
                .addFilterBefore(new LoggingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
            ;

        log.info("FilterChain Configuration - OK");

        return http.build();
    }

}
