package com.autoecole.repositories;

import com.autoecole.models.Cours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CoursRepository extends JpaRepository<Cours, Long> {
    List<Cours> findByDateDebutAfter(LocalDateTime date);
    List<Cours> findByMoniteurId(Long moniteurId);
} 