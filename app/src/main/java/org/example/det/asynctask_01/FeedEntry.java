package org.example.det.asynctask_01;

/**
 * Created by D.Alfier on 31.01.2019
 * ---------------------------------
 */

public class FeedEntry {
    private String name;
    private String artist;
    private String releaseDate;
    private String summary;
    private String imageURL;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    void setArtist(String artist) {
        this.artist = artist;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getSummary() {
        return summary;
    }

    void setSummary(String summary) {
        this.summary = summary;
    }

    String getImageURL() {
        return imageURL;
    }

    void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "name= " + name + '\n' +
                "artist= " + artist + '\n' +
                "releaseDate= " + releaseDate + '\n' +
                "imageURL= " + imageURL + '\n';
    }
}
