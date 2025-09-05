package com.goormthon.samsamejo.config;

import com.goormthon.samsamejo.constant.Constant;
import com.goormthon.samsamejo.security.exception.CustomAccessDeniedHandler;
import com.goormthon.samsamejo.security.exception.CustomAuthenticationEntryPoint;
import com.goormthon.samsamejo.security.filter.ExceptionFilter;
import com.goormthon.samsamejo.security.filter.JWTAuthenticationFilter;
import com.goormthon.samsamejo.security.handler.login.OAuth2LoginFailureHandler;
import com.goormthon.samsamejo.security.handler.login.OAuth2LoginSuccessHandler;
import com.goormthon.samsamejo.security.handler.logout.CustomLogoutProcessHandler;
import com.goormthon.samsamejo.security.handler.logout.CustomLogoutSuccessHandler;
import com.goormthon.samsamejo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final OAuth2UserService oAuth2UserService;
    private final CustomLogoutProcessHandler customLogoutProcessHandler;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JwtUtil jwtUtil;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(auth -> auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(Constant.ADMIN_ONLY_URLS.toArray(new String[0])).hasRole("ADMIN")
                        .requestMatchers(Constant.USER_AUTH_URLS.toArray(new String[0])).hasAnyRole("USER", "ADMIN")
                        .requestMatchers(Constant.PERMIT_ALL_URLS.toArray(new String[0])).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(auth -> auth
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler(oAuth2LoginFailureHandler)
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(oAuth2UserService))
                )
                .logout(auth -> auth
                        .logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(customLogoutProcessHandler)
                        .logoutSuccessHandler(customLogoutSuccessHandler)
                        .deleteCookies("access_token", "refresh_token")
                )
                .exceptionHandling(auth -> auth
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .addFilterBefore(new JWTAuthenticationFilter(jwtUtil), LogoutFilter.class)
                .addFilterBefore(new ExceptionFilter(), JWTAuthenticationFilter.class)
                .getOrBuild();
    }
}
