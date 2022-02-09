package backend.service.impl;

import backend.dao.PatientRepository;
import backend.dao.RecordRepository;
import backend.domain.Patient;
import backend.domain.Record;
import backend.dto.RecordDto;
import backend.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class RecordServiceJpa implements RecordService {

    @Autowired
    private RecordRepository recordRepo;

    @Override
    public Record createRecord(Patient patient, MultipartFile image) throws IOException {

        //tu bi mreza trebala dati dijagnozu za sliku
        String imagePath = "F:/zavrsniMreza/images/skin2.jpg";
        FileOutputStream fos = new FileOutputStream(imagePath);

        try{
            fos.write(image.getBytes());
        } catch (Exception e){
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        //pozivanje python skripte za klasifikaciju
        try {
            Process process = Runtime.getRuntime().exec("Python F:/zavrsniMreza/neuralnet.py");
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String s = null;

            while((s = stdInput.readLine()) != null) {
                sb.append(s);
            }

            /*
            while((s = stdError.readLine()) != null) {
                sb.append(s);
            }
             */

        } catch (IOException e){
            e.printStackTrace();
        }

        System.out.println(sb.toString());
        String diagnosis = sb.toString().trim();

        //proba
        //String diagnosis = "normal";

        DateFormat dateFormat = new SimpleDateFormat("d.M.yyyy. HH:mm:ss");
        Date date = new Date();

        Record record = new Record(patient.getEmail(), image.getBytes(), diagnosis, dateFormat.format(date));
        recordRepo.save(record);

        return record;
    }

    @Override
    public Set<Record> findByUsername(String username) {
        return recordRepo.findByUsername(username);
    }

    @Override
    public Record findByImageId(Long id) {
        return recordRepo.findByImageId(id);
    }

    /*
    @Override
    public Set<RecordDto> downloadRecords(String username) {

        Set<RecordDto> resources = new HashSet<>();
        Set<Record> records = recordRepo.findByUsername(username);

        for(Record record : records){
                byte[] image = record.getImage();
                resources.add(new RecordDto(image, record.getDiagnosis(), record.getDate()));
        }

        return resources;
    }
     */
}
