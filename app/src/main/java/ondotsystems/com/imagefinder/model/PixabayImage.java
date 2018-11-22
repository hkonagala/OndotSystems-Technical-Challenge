package ondotsystems.com.imagefinder.model;

public class PixabayImage {

    private String imageUrl;
    private int downloads;

    public PixabayImage(String imageUrl, int downloads) {
        this.imageUrl = imageUrl;
        this.downloads = downloads;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }
}
