package com.hongjisu.pf.portfolio.service;

import com.hongjisu.pf.portfolio.entity.Project;
import com.hongjisu.pf.portfolio.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor // final 필드를 이용한 생성자 주입
public class ProjectService {

    private final ProjectRepository projectRepository;

    // 1. 프로젝트 생성 (Create)
    @Transactional
    public Project saveProject(Project project) {
        // 실제 서비스에서는 데이터 유효성 검사 등 로직이 추가될 수 있습니다.
        return projectRepository.save(project);
    }

    // 2. 모든 프로젝트 조회 (Read - All)
    @Transactional(readOnly = true)
    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    // 3. 특정 프로젝트 ID로 조회 (Read - One)
    @Transactional(readOnly = true)
    public Project findProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + id));
    }

    // 4. 프로젝트 삭제 (Delete)
    @Transactional
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new IllegalArgumentException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }

    // ⭐️ 5. 프로젝트 수정 (Update) 메서드 - ID로 기존 엔티티를 찾고 폼 데이터를 덮어씁니다.
    @Transactional
    public Project updateProject(Long id, Project updatedProject) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + id));

        // 기존 엔티티에 새 데이터를 반영 (Setter를 이용)
        project.setTitle(updatedProject.getTitle());
        project.setDescription(updatedProject.getDescription());
        project.setTechStack(updatedProject.getTechStack());
        project.setContribution(updatedProject.getContribution());
        project.setStartDate(updatedProject.getStartDate());
        project.setEndDate(updatedProject.getEndDate());
        project.setProjectUrl(updatedProject.getProjectUrl());
        project.setImageUrl(updatedProject.getImageUrl());

        // JPA save() 메서드는 ID가 있으면 Update, 없으면 Insert를 수행합니다.
        return projectRepository.save(project);
    }
}
