package backend.dto;

import org.springframework.core.io.Resource;

import java.util.Date;

public class RecordDto {

    private byte[] image;
    private String diagnosis;
    private Date date;

    public RecordDto(byte[] image, String diagnosis, Date date) {
        this.image = image;
        this.diagnosis = diagnosis;
        this.date = date;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
