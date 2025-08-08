package com.skillmatch360.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

//import javax.persistence.*;
import java.util.Date;

import lombok.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"skills", "certifications"})
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private int experience;
    private String availability;
    private String role;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "EMPLOYEE_SKILL_MAP",
            joinColumns = @JoinColumn(name = "emp_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> skills;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Certification> certifications;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    // Helper methods for certifications
    public void addCertification(Certification certification) {
        certifications.add(certification);
        certification.setEmployee(this);
    }

    public void removeCertification(Certification certification) {
        certifications.remove(certification);
        certification.setEmployee(null);
    }

    // Helper methods for skills
    public void addSkill(Skill skill) {
        skills.add(skill);
        skill.getEmployees().add(this);
    }

    public void removeSkill(Skill skill) {
        skills.remove(skill);
        skill.getEmployees().remove(this);
    }
}
