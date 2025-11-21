package com.hongjisu.pf.portfolio.controller;

import com.hongjisu.pf.contact.service.ContactMessageService;
import com.hongjisu.pf.portfolio.entity.Project;
import com.hongjisu.pf.portfolio.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class PortfolioController {

    private final ProjectService projectService;
    private final ContactMessageService contactService;

    // 메인 페이지에서 바로 포트폴리오 보여주기
    @GetMapping("/")
    public String showPortfolio(Model model) {
        model.addAttribute("projects", projectService.findAllProjects());

        // 🔔 관리자용: 안 읽은 문의 수
        long unreadCount = contactService.countUnread();
        model.addAttribute("unreadCount", unreadCount);

        return "portfolio/home";   // templates/portfolio/home.html
    }

    @GetMapping("/projects/{id}")
    public String projectDetail(@PathVariable Long id, Model model) {
        Project project = projectService.findProjectById(id);
        model.addAttribute("project", project);
        return "portfolio/detail";   // templates/portfolio/detail.html
    }

}
