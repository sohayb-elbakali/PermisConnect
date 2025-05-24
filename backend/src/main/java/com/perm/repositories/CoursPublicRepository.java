package com.perm.repositories;

import com.perm.models.cours.CoursPublic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoursPublicRepository extends JpaRepository<CoursPublic, Long> {

    List<CoursPublic> findByActifTrue();

    @Query("SELECT cp FROM CoursPublic cp ORDER BY cp.nombreVues DESC")
    List<CoursPublic> findMostViewed();

    @Query("SELECT cp FROM CoursPublic cp WHERE cp.nombreVues > :minViews")
    List<CoursPublic> findByMinimumViews(int minViews);
}