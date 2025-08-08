package com.skillmatch360.backend.dto;

//import javax.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillDTO {
    private Long id;

    @NotBlank(message = "Skill name is required")
    private String name;

    private String proficiency; // BASIC, INTERMEDIATE, ADVANCED, EXPERT
}

