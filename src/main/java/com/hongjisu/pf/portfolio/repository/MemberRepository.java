package com.hongjisu.pf.portfolio.repository;

import com.hongjisu.pf.portfolio.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
}
