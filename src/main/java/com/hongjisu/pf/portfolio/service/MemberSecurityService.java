package com.hongjisu.pf.portfolio.service;

import com.hongjisu.pf.portfolio.entity.Member;
import com.hongjisu.pf.portfolio.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberSecurityService implements UserDetailsService {

    private final MemberRepository memberRepository;

    // Spring Security에게 사용자 정보를 제공하는 핵심 메서드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. DB에서 사용자 이름으로 Member 엔티티를 찾음
        Optional<Member> _member = memberRepository.findByUsername(username);

        // 2. 사용자가 없으면 예외 발생
        if (_member.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        Member member = _member.get();

        // 3. 사용자 역할(Role)을 기반으로 권한 리스트 생성
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Member 엔티티의 role 필드를 사용하여 권한 부여 (예: "ROLE_ADMIN")
        if (member.getRole().equals("ROLE_ADMIN")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            // 다른 역할이 있다면 추가
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        // 4. Spring Security의 User 객체로 변환하여 반환
        return new User(member.getUsername(), member.getPassword(), authorities);
    }
}