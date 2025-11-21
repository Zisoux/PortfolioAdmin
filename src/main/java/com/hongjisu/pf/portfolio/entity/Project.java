package com.hongjisu.pf.portfolio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // 프로젝트 제목
    private String description; // 프로젝트 상세 설명
    private String techStack; // 사용 기술 스택 (예: Java, Spring Boot, MySQL)
    private String contribution; // 기여 내용
    private String startDate; // 시작일
    private String endDate; // 종료일
    private String projectUrl; // 프로젝트 링크 (Github 또는 배포 URL)
    private String imageUrl; // 프로젝트 이미지 URL
}
