package com.autoecole.repositories;

import com.autoecole.models.ResultatTest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResultatTestRepository extends JpaRepository<ResultatTest, Long> {
    List<ResultatTest> findByClientId(Long clientId);
    List<ResultatTest> findByTestBlancId(Long testBlancId);
    List<ResultatTest> findByClientIdAndTestBlancId(Long clientId, Long testBlancId);
    List<ResultatTest> findByClientIdOrderByDatePassageDesc(Long clientId);
} 