package backend.dao;

import backend.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    int countByEmail(String email);
    Patient findByEmail(String email);
}
