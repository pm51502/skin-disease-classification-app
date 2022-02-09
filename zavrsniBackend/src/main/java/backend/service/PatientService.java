package backend.service;

import backend.domain.Patient;

import java.util.List;

public interface PatientService {
    List<Patient> listAll();

    Patient createPatient(Patient patient);

    Patient findByEmail(String email);

}
