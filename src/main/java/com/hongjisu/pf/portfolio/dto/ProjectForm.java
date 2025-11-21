package com.hongjisu.pf.portfolio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectForm {

    // 수정 시에만 사용하는 hidden 필드
    private Long id;

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 100, message = "제목은 100자 이내로 입력해주세요.")
    private String title; // 프로젝트 제목

    @NotBlank(message = "설명은 필수입니다.")
    @Size(max = 2000, message = "설명은 2000자 이내로 입력해주세요.")
    private String description; // 프로젝트 상세 설명

    @Size(max = 255, message = "기술 스택은 255자 이내로 입력해주세요.")
    private String techStack; // 사용 기술 스택 (예: Java, Spring Boot, MySQL)

    @Size(max = 255, message = "기여 내용은 255자 이내로 입력해주세요.")
    private String contribution; // 기여 내용

    // 날짜는 필수는 아니고, 간단한 문자열로 받되 길이만 제한
    @Size(max = 20, message = "시작일은 20자 이내로 입력해주세요.")
    private String startDate; // 시작일

    @Size(max = 20, message = "종료일은 20자 이내로 입력해주세요.")
    private String endDate; // 종료일

    @Size(max = 255, message = "URL은 255자 이내로 입력해주세요.")
    @URL(message = "유효한 URL 형식으로 입력해주세요.")
    private String projectUrl; // 프로젝트 링크 (Github 또는 배포 URL)

    @Size(max = 300)
    private String imageUrl;   // 🔹 DB에 저장할 경로 (/uploads/xxx.png)

    // 🔹 실제 업로드 파일 (DB에는 저장 X)
    private MultipartFile imageFile;
}
