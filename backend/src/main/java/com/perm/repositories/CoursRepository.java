package com.perm.repositories;

import com.perm.models.cours.Cours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoursRepository extends JpaRepository<Cours, Long> {

    List<Cours> findByAutoEcoleId(Long autoEcoleId);

    List<Cours> findByTitreContainingIgnoreCase(String titre);

    List<Cours> findByActifTrue();

    @Query("SELECT c FROM Cours c WHERE TYPE(c) = :type")
    List<Cours> findByType(@Param("type") Class<?> type);

    @Query("SELECT c FROM Cours c WHERE c.autoEcole.id = :autoEcoleId AND c.actif = true")
    List<Cours> findActiveByAutoEcole(@Param("autoEcoleId") Long autoEcoleId);
}