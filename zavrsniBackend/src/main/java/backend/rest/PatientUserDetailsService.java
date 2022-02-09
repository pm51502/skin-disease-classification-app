package backend.rest;

import backend.dao.PatientRepository;
import backend.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;

@Service
public class PatientUserDetailsService implements UserDetailsService {

    @Autowired
    private PatientRepository patientRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Patient patient = patientRepo.findByEmail(username);

        if(patient != null){
            System.out.println(patient.getEmail() + " " + patient.getPassword());
            return new User(patient.getEmail(), patient.getPassword(), commaSeparatedStringToAuthorityList(patient.getRole().getRole()));
        }

        throw new UsernameNotFoundException("No patient with email " + username);
    }
}
