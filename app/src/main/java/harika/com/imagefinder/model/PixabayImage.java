package harika.com.imagefinder.model;

import android.os.Parcel;

/*
*
* Model class
 */
public class PixabayImage {

    private String imageUrl;
    private int downloads;

    public PixabayImage(String imageUrl, int downloads) {
        this.imageUrl = imageUrl;
        this.downloads = downloads;
    }

    protected PixabayImage(Parcel in) {
        imageUrl = in.readString();
        downloads = in.readInt();
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
