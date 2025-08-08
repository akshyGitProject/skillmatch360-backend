package com.skillmatch360.backend.service.Impl;

//import com.skillmatch360.backend.dto.SkillDTO;
import com.skillmatch360.backend.dto.SkillDTO;
import com.skillmatch360.backend.model.Skill;
import com.skillmatch360.backend.repository.SkillRepository;
import com.skillmatch360.backend.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SkillServiceImpl implements SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Override
    public SkillDTO createSkill(SkillDTO skillDTO) {
        Skill skill = new Skill();
        skill.setName(skillDTO.getName());
        skill.setProficiency(skillDTO.getProficiency());
        Skill savedSkill = skillRepository.save(skill);
        return convertToDTO(savedSkill);
    }

    @Override
    public List<SkillDTO> getAllSkills() {
        return skillRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SkillDTO convertToDTO(Skill skill) {
        return SkillDTO.builder()
                .id(skill.getId())
                .name(skill.getName())
                .proficiency(skill.getProficiency())
                .build();
    }

    @Override
    public Set<Skill> getSkillsByIds(Set<Long> skillIds) {
        return (Set<Skill>) skillRepository.findAllById(skillIds);
    }
}
