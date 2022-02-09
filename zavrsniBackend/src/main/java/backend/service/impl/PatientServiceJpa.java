package backend.service.impl;

import backend.dao.PatientRepository;
import backend.domain.Patient;
import backend.domain.enums.Role;
import backend.service.RequestDeniedException;
import backend.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class PatientServiceJpa implements PatientService {

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private PasswordEncoder pwdencoder;

    @Override
    public List<Patient> listAll() {
        return patientRepo.findAll();
    }

    private static final String EMAIL_FORMAT = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    @Override
    public Patient createPatient(Patient patient) {
        Assert.notNull(patient, "Patient object must be given");
        Assert.isNull(patient.getId(), "Patient ID must be null, not " + patient.getId());

        String email = patient.getEmail();
        Assert.hasText(email, "Email must be given");
        Assert.isTrue(email.matches(EMAIL_FORMAT), "Email must be in valid format");

        if(patientRepo.countByEmail(patient.getEmail()) > 0)
            throw new RequestDeniedException("Patient with email " + patient.getEmail() + " alredy exists");

        String password = patient.getPassword();
        System.out.println(password);
        password = pwdencoder.encode(password);
        patient.setPassword(password);

        patient.setRole(Role.PATIENT);

        return patientRepo.save(patient);
    }

    @Override
    public Patient findByEmail(String email) {
        Assert.notNull(email, "Email must be given");
        return patientRepo.findByEmail(email);
    }
}
