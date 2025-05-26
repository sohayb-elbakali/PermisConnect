package com.autoecole.repositories;

import com.autoecole.models.Cours;
import com.autoecole.models.CourseType;
import com.autoecole.models.CoursPrive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CoursRepository extends JpaRepository<Cours, Long> {
    List<Cours> findByDateDebutAfter(LocalDateTime date);
    List<Cours> findByMoniteurId(Long moniteurId);
    List<Cours> findByCourseType(CourseType courseType);
    List<CoursPrive> findByCourseTypeAndAutoEcoleId(CourseType courseType, Long autoEcoleId);
} 