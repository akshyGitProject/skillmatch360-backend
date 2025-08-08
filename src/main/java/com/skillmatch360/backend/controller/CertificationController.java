package com.skillmatch360.backend.controller;

import com.skillmatch360.backend.dto.CertificationDTO;
import com.skillmatch360.backend.service.CertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certifications")
public class CertificationController {

    @Autowired
    private CertificationService certificationService;

    @GetMapping
    public ResponseEntity<List<CertificationDTO>> getAllCertifications() {
        return ResponseEntity.ok(certificationService.getAllCertifications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificationDTO> getCertificationById(@PathVariable Long id) {
        return ResponseEntity.ok(certificationService.getCertificationById(id));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<CertificationDTO>> getCertificationsByEmployee(
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(certificationService.getCertificationsByEmployee(employeeId));
    }

    @PostMapping
    public ResponseEntity<CertificationDTO> createCertification(
            @RequestBody CertificationDTO certificationDTO) {
        return new ResponseEntity<>(
                certificationService.createCertification(certificationDTO),
                HttpStatus.CREATED);
    }
}