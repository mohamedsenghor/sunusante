package dev.sunusante.patient.repository;

import dev.sunusante.patient.domain.PatientConsent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

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
