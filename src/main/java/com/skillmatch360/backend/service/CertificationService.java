package com.skillmatch360.backend.service;

import com.skillmatch360.backend.dto.CertificationDTO;
import com.skillmatch360.backend.model.Certification;
import java.util.List;

public interface CertificationService {
    CertificationDTO createCertification(CertificationDTO certificationDTO);
    List<CertificationDTO> getAllCertifications();
    CertificationDTO getCertificationById(Long id);
    List<CertificationDTO> getCertificationsByEmployee(Long employeeId);
    CertificationDTO convertToDTO(Certification certification); // Add this method
}