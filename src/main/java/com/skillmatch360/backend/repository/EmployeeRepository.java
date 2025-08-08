package com.skillmatch360.backend.repository;


//import com.skillmatch360.model.Employee;
import com.skillmatch360.backend.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Set;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findBySkills_IdIn(Set<Long> skillIds);
    List<Employee> findByExperienceGreaterThanEqual(int experience);
    List<Employee> findByAvailability(String availability);

    @Query("SELECT e FROM Employee e JOIN e.skills s WHERE s.id IN :skillIds GROUP BY e HAVING COUNT(s) = :skillCount")
    List<Employee> findByAllSkills(@Param("skillIds") Set<Long> skillIds, @Param("skillCount") long skillCount);

    boolean existsByEmail(String email);
}