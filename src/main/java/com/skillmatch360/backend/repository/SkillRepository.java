package com.skillmatch360.backend.repository;

import com.skillmatch360.backend.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    Set<Skill> findAllByIdIn(Set<Long> ids);
}
