package dev.sunusante.patient.repository;

import dev.sunusante.patient.domain.PatientConsent;
import dev.sunusante.patient.domain.enumeration.ConsentStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

/**
 * Spring Data JPA repository for the PatientConsent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PatientConsentRepository extends JpaRepository<PatientConsent, Long> {
    java.util.Optional<PatientConsent> findFirstByPatient_PseudoAndDoctorLoginAndStatusAndExpiryDateAfterOrderByConsentDateDesc(
        String pseudo,
        String doctorLogin,
        dev.sunusante.patient.domain.enumeration.ConsentStatus status,
        java.time.Instant now
    );
}
