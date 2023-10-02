package yukewu.project.backend.dao;

public class Image {
    private String userId;
    private byte[] photo;

    public Image(String userId, byte[] photo) {
        this.userId = userId;
        this.photo = photo;
    }

    public Image(){
        
    }

    public String getUserId() {
        return userId;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}
