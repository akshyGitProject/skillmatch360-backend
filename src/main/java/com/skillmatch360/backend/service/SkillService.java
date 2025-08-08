package com.skillmatch360.backend.service;

import com.skillmatch360.backend.dto.SkillDTO;
import com.skillmatch360.backend.model.Skill;

import java.util.List;
import java.util.Set;

public interface SkillService {
    SkillDTO createSkill(SkillDTO skillDTO);
    List<SkillDTO> getAllSkills();
    SkillDTO convertToDTO(Skill skill);
    Set<Skill> getSkillsByIds(Set<Long> skillIds);
}
