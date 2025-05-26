package com.autoecole.repositories;

import com.autoecole.models.ClientCourseView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientCourseViewRepository extends JpaRepository<ClientCourseView, Long> {
    List<ClientCourseView> findByClientId(Long clientId);
    Optional<ClientCourseView> findByClientIdAndCourseId(Long clientId, Long courseId);
} 