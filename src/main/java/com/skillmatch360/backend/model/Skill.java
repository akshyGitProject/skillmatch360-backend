package com.skillmatch360.backend.model;

//import javax.persistence.*;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "SKILL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "employees")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String proficiency;

    @ManyToMany(mappedBy = "skills", fetch = FetchType.LAZY)
    private Set<Employee> employees;

    // Helper method for bidirectional relationship
    public void addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.getSkills().add(this);
    }

    // Helper method for bidirectional relationship
    public void removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.getSkills().remove(this);
    }
}
