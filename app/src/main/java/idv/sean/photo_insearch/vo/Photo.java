package idv.sean.photo_insearch.vo;


public class Photo{
    private int photo;
    private String title;

    public Photo() {
    }

    public Photo(int photo, String title) {
        this.photo = photo;
        this.title = title;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
