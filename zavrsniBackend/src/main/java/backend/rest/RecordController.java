package backend.rest;

import backend.domain.Patient;
import backend.dto.RecordDto;
import backend.service.PatientService;
import backend.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private PatientService patientService;

    @PostMapping("/create")
    public ResponseEntity<?> createRecord(String username, @RequestParam("image")MultipartFile image) throws IOException {

        Patient patient = patientService.findByEmail(username);
        if(patient != null)
            return new ResponseEntity<>(recordService.createRecord(patient, image), HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /*
    @PostMapping("/create")
    public ResponseEntity<?> createRecord(@AuthenticationPrincipal User user, @RequestBody byte[] image) throws IOException {

        System.out.println(user.getUsername());
        System.out.println(user.getPassword());

        System.out.println("Create record!!!!!!!!!!!!!!!");


        if(image.length > 0){
            System.out.println("image sent");
            return new ResponseEntity<>(HttpStatus.OK);
        }


        return new ResponseEntity<>(HttpStatus.OK);


        Patient patient = patientService.findByEmail(user.getUsername());
        return new ResponseEntity<>(recordService.createRecord(patient, image), HttpStatus.OK);

    }
     */

    @GetMapping("/get")
    public ResponseEntity<?>  getRecords(@RequestParam("username") String username){

        Patient patient = patientService.findByEmail(username);
        return new ResponseEntity<>(recordService.findByUsername(patient.getEmail()), HttpStatus.OK);

    }

    /*
    @GetMapping("/get/id")
    public ResponseEntity<?>  getRecordById(@RequestParam("id") Long id){
        byte[] image = recordService.findByImageId(id).getImage();

        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<?>  getRecords(@AuthenticationPrincipal User user){

        Patient patient = patientService.findByEmail(user.getUsername());
        return new ResponseEntity<>(recordService.findByUsername(patient.getEmail()), HttpStatus.OK);

    }

    @GetMapping("/download")
    public Set<RecordDto> downloadRecords(@AuthenticationPrincipal User user){

        Patient patient = patientService.findByEmail(user.getUsername());
        return recordService.downloadRecords(patient.getEmail());

    }
     */

}
