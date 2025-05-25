package com.autoecole.repositories;

import com.autoecole.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByTestBlancId(Long testBlancId);
    
    @Query("SELECT q FROM Question q WHERE q.testBlanc.id = :testId ORDER BY COALESCE(q.ordre, 999999)")
    List<Question> findByTestBlancIdOrderByOrdreAsc(@Param("testId") Long testBlancId);
} 