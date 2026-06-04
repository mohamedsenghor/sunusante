package dev.sunusante.dmp.client;

import dev.sunusante.dmp.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "sunusantePatientService", configuration = FeignConfiguration.class)
public interface PatientConsentClient {
    @GetMapping("/api/patient-consents/check")
    Boolean checkActiveConsent(@RequestParam("patientPseudo") String patientPseudo, @RequestParam("doctorLogin") String doctorLogin);
}
