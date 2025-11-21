package com.hongjisu.pf.portfolio.controller;

import com.hongjisu.pf.portfolio.dto.ProjectForm;
import com.hongjisu.pf.portfolio.entity.Project;
import com.hongjisu.pf.portfolio.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/admin/projects")
@RequiredArgsConstructor
public class AdminProjectController {

    private final ProjectService projectService;

    // FileConfig에서 쓰는 것과 동일한 upload-dir
    @Value("${file.upload-dir:${user.home}/uploads}")
    private String uploadDir;

    // 1. 프로젝트 목록 페이지 (Read All)
    @GetMapping
    public String listProjects(Model model) {
        model.addAttribute("projects", projectService.findAllProjects());
        return "admin/project/list";
    }

    // 2. 프로젝트 등록 폼 페이지
    @GetMapping("/new")
    public String createProjectForm(Model model) {
        model.addAttribute("projectForm", new ProjectForm());
        return "admin/project/form";
    }

    // 3. 프로젝트 등록 처리 (Create)
    @PostMapping("/new")
    public String saveProject(@Valid @ModelAttribute("projectForm") ProjectForm projectForm,
                              BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()) {
            return "admin/project/form";
        }

        // 🔹 이미지 업로드 처리
        String imageUrl = handleImageUpload(projectForm.getImageFile(), projectForm.getImageUrl());
        projectForm.setImageUrl(imageUrl);

        Project project = toEntity(projectForm);
        projectService.saveProject(project);

        return "redirect:/admin/projects";
    }

    // 4. 프로젝트 삭제 처리 (Delete)
    @PostMapping("/{id}/delete")
    public String deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return "redirect:/admin/projects";
    }

    // 5. 프로젝트 수정 폼 페이지 (Update Form)
    @GetMapping("/{id}/edit")
    public String editProjectForm(@PathVariable Long id, Model model) {
        Project project = projectService.findProjectById(id);

        ProjectForm form = toForm(project);
        model.addAttribute("projectForm", form);

        return "admin/project/form";
    }

    // 6. 프로젝트 수정 처리 (Update Logic)
    @PostMapping("/{id}/edit")
    public String updateProject(@PathVariable Long id,
                                @Valid @ModelAttribute("projectForm") ProjectForm projectForm,
                                BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()) {
            return "admin/project/form";
        }

        String imageUrl = handleImageUpload(projectForm.getImageFile(), projectForm.getImageUrl());
        projectForm.setImageUrl(imageUrl);

        Project project = toEntity(projectForm);
        projectService.updateProject(id, project);

        return "redirect:/admin/projects";
    }

    // ================== 매핑 유틸 메서드 ==================

    // DTO → Entity
    private Project toEntity(ProjectForm form) {
        Project project = new Project();
        project.setId(form.getId());
        project.setTitle(form.getTitle());
        project.setDescription(form.getDescription());
        project.setTechStack(form.getTechStack());
        project.setContribution(form.getContribution());
        project.setStartDate(form.getStartDate());
        project.setEndDate(form.getEndDate());
        project.setProjectUrl(form.getProjectUrl());
        project.setImageUrl(form.getImageUrl());
        return project;
    }

    // Entity → DTO
    private ProjectForm toForm(Project project) {
        ProjectForm form = new ProjectForm();
        form.setId(project.getId());
        form.setTitle(project.getTitle());
        form.setDescription(project.getDescription());
        form.setTechStack(project.getTechStack());
        form.setContribution(project.getContribution());
        form.setStartDate(project.getStartDate());
        form.setEndDate(project.getEndDate());
        form.setProjectUrl(project.getProjectUrl());
        form.setImageUrl(project.getImageUrl());
        // imageFile은 화면에서 새로 업로드할 때만 쓰니 여기선 null 그대로
        return form;
    }

    // ================== 이미지 업로드 처리 ==================

    /**
     * - 이미지 파일 업로드가 있으면: 파일 저장 후 "/uploads/파일명" 리턴
     * - 파일 업로드는 없고, 텍스트 URL만 있으면: 그 URL 그대로 사용
     * - 둘 다 없으면: null
     */
    private String handleImageUpload(MultipartFile imageFile, String currentOrInputUrl) throws IOException {
        // 1) 새 파일이 올라왔으면 → 로컬에 저장 후 /uploads/ 경로 리턴
        if (imageFile != null && !imageFile.isEmpty()) {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalName = imageFile.getOriginalFilename();
            String ext = "";
            if (originalName != null && originalName.contains(".")) {
                ext = originalName.substring(originalName.lastIndexOf("."));
            }

            String savedName = UUID.randomUUID() + ext;
            Path target = uploadPath.resolve(savedName);

            Files.copy(imageFile.getInputStream(), target);

            // FileConfig에서 /uploads/** → 실제 디렉토리로 매핑되어 있으므로
            // 브라우저에서는 이 URL로 접근
            return "/uploads/" + savedName;
        }

        // 2) 파일은 없지만 기존/입력된 imageUrl이 있으면 그대로 사용
        if (currentOrInputUrl != null && !currentOrInputUrl.isBlank()) {
            return currentOrInputUrl.trim();
        }

        // 3) 아무것도 없으면 null
        return null;
    }
}
