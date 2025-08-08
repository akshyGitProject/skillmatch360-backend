package com.skillmatch360.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificationDTO {
    private Long id;

    @NotBlank(message = "Certification name is required")
    private String name;

    @NotBlank(message = "Organization name is required")
    private String orgName;

    @NotBlank(message = "Issue date is required")
    private String issueDate; // Format: "yyyy-MM-dd"

    private String expiryDate; // Nullable, Format: "yyyy-MM-dd"

    private Long employeeId; // FK to Employee
}