package com.perm.repositories;

import com.perm.models.cours.CoursPrive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoursPriveRepository extends JpaRepository<CoursPrive, Long> {

    @Query("SELECT cp FROM CoursPrive cp WHERE cp.autoEcole.id = :autoEcoleId")
    List<CoursPrive> findByAutoEcoleId(@Param("autoEcoleId") Long autoEcoleId);

    @Query("SELECT cp FROM CoursPrive cp WHERE cp.actif = true AND cp.autoEcole.id = :autoEcoleId")
    List<CoursPrive> findActiveByAutoEcole(@Param("autoEcoleId") Long autoEcoleId);
}
