package com.hongjisu.pf.config;

import com.hongjisu.pf.portfolio.service.MemberSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberSecurityService memberSecurityService; // UserDetailsService 구현체

    // 비밀번호 인코더
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager 설정 (DB 사용자 + 비밀번호 인코더)
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder
                .userDetailsService(memberSecurityService)
                .passwordEncoder(passwordEncoder());

        return authBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 과제/관리자 화면 테스트 편하려면 일단 CSRF 끄고 시작
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // ✅ 포트폴리오 메인 & 정적 리소스 → 모두 허용
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/assets/**",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()

                        // ✅ 로그인 페이지는 모두 허용
                        .requestMatchers("/admin/login").permitAll()

                        // ✅ 관리자 영역은 ADMIN 권한만 접근 가능
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 그 외는 일단 모두 허용 (필요하면 여기서 더 잠그기)
                        .anyRequest().permitAll()
                )

                // ✅ 폼 로그인 설정
                .formLogin(form -> form
                        .loginPage("/admin/login")          // GET: 로그인 폼
                        .loginProcessingUrl("/admin/login") // POST: 로그인 처리
                        .defaultSuccessUrl("/admin/projects", true)
                        .usernameParameter("username")      // 폼의 name과 맞추기
                        .passwordParameter("password")
                        .permitAll()
                )

                // ✅ 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                );

        return http.build();
    }
}
