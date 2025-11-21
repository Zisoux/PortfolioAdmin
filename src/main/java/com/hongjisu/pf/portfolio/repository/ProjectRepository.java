package com.hongjisu.pf.portfolio.repository;

import com.hongjisu.pf.portfolio.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
