package dev.sunusante.patient.repository;

import dev.sunusante.patient.domain.Patient;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Patient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findOneByPseudo(String pseudo);
}
