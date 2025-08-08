package com.skillmatch360.backend.dto;

import lombok.*;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {
    private Long id;
    private String name;
    private String email;
    private int experience;
    private String availability;
    private String role;
    private Set<SkillDTO> skills;
    private Set<CertificationDTO> certifications;
}
