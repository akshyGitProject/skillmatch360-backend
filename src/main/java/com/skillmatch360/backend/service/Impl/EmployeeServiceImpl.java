package com.skillmatch360.backend.service.Impl;

import com.skillmatch360.backend.dto.*;
import com.skillmatch360.backend.exception.ResourceNotFoundException;
import com.skillmatch360.backend.model.Employee;
import com.skillmatch360.backend.model.Skill;
import com.skillmatch360.backend.repository.EmployeeRepository;
import com.skillmatch360.backend.repository.SkillRepository;
import com.skillmatch360.backend.service.CertificationService;
import com.skillmatch360.backend.service.EmployeeService;
import com.skillmatch360.backend.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SkillService skillService;

    @Autowired
    private CertificationService certificationService;

    @Override
    public EmployeeResponseDTO createEmployee(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Employee employee = convertToEntity(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        return convertToResponseDTO(savedEmployee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDTO getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(this::convertToResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    @Override
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        if (!existingEmployee.getEmail().equals(employeeDTO.getEmail()) &&
                employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        existingEmployee.setName(employeeDTO.getName());
        existingEmployee.setEmail(employeeDTO.getEmail());
        existingEmployee.setExperience(employeeDTO.getExperience());
        existingEmployee.setAvailability(employeeDTO.getAvailability());
        existingEmployee.setRole(employeeDTO.getRole());

        // Update skills
        Set<Skill> updatedSkills = new HashSet<>(skillRepository.findAllById(employeeDTO.getSkillIds()));
        existingEmployee.getSkills().clear();
        updatedSkills.forEach(skill -> existingEmployee.addSkill(skill));

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return convertToResponseDTO(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        // Remove employee from all skills first
        new HashSet<>(employee.getSkills()).forEach(skill -> skill.removeEmployee(employee));
        employeeRepository.delete(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private Employee convertToEntity(EmployeeDTO employeeDTO) {
        Employee employee = Employee.builder()
                .id(employeeDTO.getId())
                .name(employeeDTO.getName())
                .email(employeeDTO.getEmail())
                .experience(employeeDTO.getExperience())
                .availability(employeeDTO.getAvailability())
                .role(employeeDTO.getRole())
                .skills(new HashSet<>())
                .certifications(new HashSet<>())
                .build();

        // Add skills using the proper bidirectional method
        skillRepository.findAllById(employeeDTO.getSkillIds())
                .forEach(skill -> employee.addSkill(skill));

        return employee;
    }

    private EmployeeResponseDTO convertToResponseDTO(Employee employee) {
        if (employee == null) {
            return null;
        }

        // Convert skills without employee references
        Set<SkillDTO> skillDTOs = employee.getSkills().stream()
                .map(skill -> SkillDTO.builder()
                        .id(skill.getId())
                        .name(skill.getName())
                        .proficiency(skill.getProficiency())
                        .build())
                .collect(Collectors.toSet());

        // Convert certifications without employee references
        Set<CertificationDTO> certificationDTOs = employee.getCertifications() != null ?
                employee.getCertifications().stream()
                        .map(cert -> CertificationDTO.builder()
                                .id(cert.getId())
                                .name(cert.getName())
                                .orgName(cert.getOrgName())
                                .issueDate(cert.getIssueDate().toString())
                                .expiryDate(cert.getExpiryDate() != null ?
                                        cert.getExpiryDate().toString() : null)
                                .build())
                        .collect(Collectors.toSet()) :
                Collections.emptySet();

        return EmployeeResponseDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .experience(employee.getExperience())
                .availability(employee.getAvailability())
                .role(employee.getRole())
                .skills(skillDTOs)
                .certifications(certificationDTOs)
                .build();
    }
}