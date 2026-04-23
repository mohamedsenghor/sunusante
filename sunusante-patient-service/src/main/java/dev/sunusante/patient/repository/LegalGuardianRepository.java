package dev.sunusante.patient.repository;

import dev.sunusante.patient.domain.LegalGuardian;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LegalGuardian entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LegalGuardianRepository extends JpaRepository<LegalGuardian, Long> {}
