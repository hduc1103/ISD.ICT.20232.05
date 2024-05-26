package org.example.models;

import java.util.Date;

public class CD extends Media {

    /**
     * Single responsibility:
     * CD has 1 responsibility: store data for a CD media type.
     */
    private String artist;
    private Date releasedDate;
    private String recordLabel;
    private String musicType;

    public CD(int id, String name, double price, boolean rushDelivery, double weight, int available, String imageUrl, String category, String artist, Date releasedDate, String recordLabel, String musicType) {
        super(id, name, price, rushDelivery, weight, available, imageUrl, category);
        this.artist = artist;
        this.releasedDate = releasedDate;
        this.recordLabel = recordLabel;
        this.musicType = musicType;
    }

    // Getters and Setters
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Date getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(Date releasedDate) {
        this.releasedDate = releasedDate;
    }

    public String getRecordLabel() {
        return recordLabel;
    }

    public void setRecordLabel(String recordLabel) {
        this.recordLabel = recordLabel;
    }

    public String getMusicType() {
        return musicType;
    }

    public void setMusicType(String musicType) {
        this.musicType = musicType;
    }
}
