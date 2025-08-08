package com.skillmatch360.backend.service.impl;

import com.skillmatch360.backend.dto.CertificationDTO;
import com.skillmatch360.backend.exception.ResourceNotFoundException;
import com.skillmatch360.backend.model.Certification;
import com.skillmatch360.backend.model.Employee;
import com.skillmatch360.backend.repository.CertificationRepository;
import com.skillmatch360.backend.repository.EmployeeRepository;
import com.skillmatch360.backend.service.CertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CertificationServiceImpl implements CertificationService {

    @Autowired
    private CertificationRepository certificationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public CertificationDTO createCertification(CertificationDTO certificationDTO) {
        validateCertificationDTO(certificationDTO);

        Employee employee = employeeRepository.findById(certificationDTO.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + certificationDTO.getEmployeeId()));

        Certification certification = mapToEntity(certificationDTO, employee);
        Certification savedCertification = certificationRepository.save(certification);
        return convertToDTO(savedCertification);
    }

    @Override
    public List<CertificationDTO> getAllCertifications() {
        return certificationRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CertificationDTO getCertificationById(Long id) {
        return certificationRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Certification not found with id: " + id));
    }

    @Override
    public List<CertificationDTO> getCertificationsByEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException("Employee not found with id: " + employeeId);
        }

        return certificationRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CertificationDTO convertToDTO(Certification certification) {
        if (certification == null) {
            return null;
        }

        return CertificationDTO.builder()
                .id(certification.getId())
                .name(certification.getName())
                .orgName(certification.getOrgName())
                .issueDate(certification.getIssueDate().toString())
                .expiryDate(certification.getExpiryDate() != null ?
                        certification.getExpiryDate().toString() : null)
                .employeeId(certification.getEmployee().getId())
                .build();
    }

    private void validateCertificationDTO(CertificationDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Certification data cannot be null");
        }
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Certification name is required");
        }
        if (dto.getIssueDate() == null || dto.getIssueDate().trim().isEmpty()) {
            throw new IllegalArgumentException("Issue date is required");
        }
        if (dto.getEmployeeId() == null) {
            throw new IllegalArgumentException("Employee ID is required");
        }
    }

    private LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString, dateFormatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use yyyy-MM-dd");
        }
    }

    private Certification mapToEntity(CertificationDTO dto, Employee employee) {
        return Certification.builder()
                .name(dto.getName())
                .orgName(dto.getOrgName())
                .issueDate(parseDate(dto.getIssueDate()))
                .expiryDate(dto.getExpiryDate() != null ?
                        parseDate(dto.getExpiryDate()) : null)
                .employee(employee)
                .build();
    }

}