package com.skillmatch360.backend.service;


import com.skillmatch360.backend.dto.SkillDTO;
import java.util.List;

public interface SkillService {
    SkillDTO createSkill(SkillDTO skillDTO);
    List<SkillDTO> getAllSkills();
}