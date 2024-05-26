package org.example.models;

import java.util.Date;

/**
 * Single responsibility:
 * The DVD class is responsible for storing data related to a DVD.
 */
public class DVD extends Media {
    private String discType;
    private String director;
    private String studio;
    private Date releasedDate;
    private String subtitle;
    private String runtime;

    public DVD(int id, String name, double price, boolean rushDelivery, double weight, int available, 
               String imageUrl, String category, String discType, String director, 
               String studio, Date releasedDate, String subtitle, String runtime) {
        super(id, name, price, rushDelivery, weight, available, imageUrl, category);
        this.discType = discType;
        this.director = director;
        this.studio = studio;
        this.releasedDate = releasedDate;
        this.subtitle = subtitle;
        this.runtime = runtime;
    }

    // Getter and setter methods
    public String getDiscType() {
        return discType;
    }

    public void setDiscType(String discType) {
        this.discType = discType;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public Date getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(Date releasedDate) {
        this.releasedDate = releasedDate;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }
}
