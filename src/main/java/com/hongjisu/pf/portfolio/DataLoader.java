package com.hongjisu.pf.portfolio;

import com.hongjisu.pf.portfolio.entity.Member;
import com.hongjisu.pf.portfolio.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner initData(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // "admin" 계정이 없으면 생성
            if (memberRepository.findByUsername("admin").isEmpty()) {
                // 비밀번호: 1234
                Member admin = new Member("admin", passwordEncoder.encode("1234"), "ROLE_ADMIN");
                memberRepository.save(admin);
                System.out.println("--- [초기 관리자 계정 생성 완료] ID: admin, PW: 1234 ---");
            }
        };
    }
}
