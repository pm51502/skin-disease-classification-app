package backend.service;

import backend.domain.Patient;
import backend.domain.Record;
import backend.dto.RecordDto;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Set;

public interface RecordService {

    Record createRecord(Patient patient, MultipartFile image) throws IOException;

    Set<Record> findByUsername(String username);

    Record findByImageId(Long id);

    //Set<RecordDto> downloadRecords(String username);
}
