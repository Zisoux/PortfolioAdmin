package com.hongjisu.pf.portfolio.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username; // 사용자 ID (로그인에 사용)

    private String password; // 암호화된 비밀번호

    // 역할 정보 (ROLE_ADMIN 등)
    private String role;

    // 생성자 (테스트용)
    public Member(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
