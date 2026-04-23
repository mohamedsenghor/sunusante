package dev.sunusante.dmp.repository;

import dev.sunusante.dmp.domain.MedicalEntry;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MedicalEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicalEntryRepository extends JpaRepository<MedicalEntry, Long> {}
