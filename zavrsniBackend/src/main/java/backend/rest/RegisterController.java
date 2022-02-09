package backend.rest;

import backend.domain.Patient;
import backend.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private PatientService patientService;

    @GetMapping
    public String registrationPatientTester(){
        return patientService.listAll().toString();
    }

    @PostMapping("")
    public Patient createPatient(@RequestBody Patient patient){
        System.out.println(patient.getEmail());
        System.out.println(patient.getPassword());
        return patientService.createPatient(patient);
    }

}
