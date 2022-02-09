package hr.fer.rpp.classificationapp.models;

public class Image {

    private byte[] content;

    public Image(byte[] content) {
        this.content = content;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
