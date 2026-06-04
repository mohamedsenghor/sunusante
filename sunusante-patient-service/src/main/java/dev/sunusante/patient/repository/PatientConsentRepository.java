package dev.sunusante.patient.repository;

import dev.sunusante.patient.domain.PatientConsent;
import dev.sunusante.patient.domain.enumeration.ConsentStatus;
import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PatientConsent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PatientConsentRepository extends JpaRepository<PatientConsent, Long> {
    Optional<PatientConsent> findFirstByPatientPseudoAndDoctorLoginAndStatusAndExpiryDateAfterOrderByConsentDateDesc(
        String patientPseudo,
        String doctorLogin,
        ConsentStatus status,
        Instant now
    );
}
