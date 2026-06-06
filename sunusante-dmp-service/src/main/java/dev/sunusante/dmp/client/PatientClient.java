package dev.sunusante.dmp.client;

import dev.sunusante.dmp.config.FeignConfiguration;
import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "sunusantePatientService", configuration = FeignConfiguration.class)
public interface PatientClient {
    @GetMapping("/api/patients/pseudo/{pseudo}")
    Optional<PatientDTO> getPatientByPseudo(@PathVariable("pseudo") String pseudo);

    class PatientDTO {
        private String pseudo;
        private String email;
        private String firstName;
        private String lastName;

        public String getPseudo() { return pseudo; }
        public void setPseudo(String pseudo) { this.pseudo = pseudo; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
    }
}
