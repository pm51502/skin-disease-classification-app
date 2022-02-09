package backend.rest;

import backend.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/loggedin")
    public ResponseEntity<?> userIsLoggedIn(@AuthenticationPrincipal User user){
        if(user == null)
            return new ResponseEntity<>(false, HttpStatus.OK);

        return new ResponseEntity<>(patientService.findByEmail(user.getUsername()), HttpStatus.OK );
    }

}
