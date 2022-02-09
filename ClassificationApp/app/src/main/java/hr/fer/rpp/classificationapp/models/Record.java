package hr.fer.rpp.classificationapp.models;

public class Record {

    private Long id;
    private String username;
    private String diagnosis;
    private String date;

    public Record(Long id, String username, String diagnosis, String date) {
        this.id = id;
        this.username = username;
        this.diagnosis = diagnosis;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getDate() {
        return date;
    }

    /*
    private int id;
    private String username;
    private String image;
    private String diagnosis;
    private String date;

    public Record(int id, String username, String image, String diagnosis, String date) {
        this.id = id;
        this.username = username;
        this.image = image;
        this.diagnosis = diagnosis;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getImage() {
        return image;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getDate() {
        return date;
    }

     */
}
