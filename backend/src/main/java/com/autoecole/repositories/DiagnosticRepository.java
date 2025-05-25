package com.autoecole.repositories;

import com.autoecole.models.Diagnostic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnosticRepository extends JpaRepository<Diagnostic, Long> {
    List<Diagnostic> findByClientId(Long clientId);

    List<Diagnostic> findByClientIdOrderByDateDiagnosticDesc(Long clientId);
}