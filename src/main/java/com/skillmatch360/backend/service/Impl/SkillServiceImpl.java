package com.skillmatch360.backend.service.Impl;

import com.skillmatch360.backend.dto.SkillDTO;
import com.skillmatch360.backend.model.Skill;
import com.skillmatch360.backend.repository.SkillRepository;
import com.skillmatch360.backend.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillServiceImpl implements SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Override
    public SkillDTO createSkill(SkillDTO dto) {
        if (skillRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Skill already exists!");
        }

        Skill skill = new Skill(dto.getName());
        Skill saved = skillRepository.save(skill);
        return new SkillDTO(saved.getId(), saved.getName());
    }

    @Override
    public List<SkillDTO> getAllSkills() {
        return skillRepository.findAll()
                .stream()
                .map(skill -> new SkillDTO(skill.getId(), skill.getName()))
                .collect(Collectors.toList());
    }
}
