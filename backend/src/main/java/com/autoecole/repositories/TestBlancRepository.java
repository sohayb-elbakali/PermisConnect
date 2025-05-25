package com.autoecole.repositories;

import com.autoecole.models.TestBlanc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestBlancRepository extends JpaRepository<TestBlanc, Long> {
    List<TestBlanc> findByNombreQuestionsLessThanEqual(Integer nombreQuestions);
    List<TestBlanc> findByScoreMinimumLessThanEqual(Integer scoreMinimum);
} 