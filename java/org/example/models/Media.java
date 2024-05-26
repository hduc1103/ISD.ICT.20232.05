package org.example.models;

public class Media {
    /**
     * Single responsibility:
     * This class has one responsibility: store data for media items.
     */
    private int id;
    private String name;
    private double price;
    private boolean rushDelivery;
    private double weight;
    private int available;
    private String imageUrl;
    private String category;

    public Media(String name, double price, boolean rushDelivery, double weight, int available, String imageUrl, String category) {
        this(0, name, price, rushDelivery, weight, available, imageUrl, category);
    }

    public Media(int id, String name, double price, boolean rushDelivery, double weight, int available, String imageUrl, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.rushDelivery = rushDelivery;
        this.weight = weight;
        this.available = available;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isRushDelivery() {
        return rushDelivery;
    }

    public void setRushDelivery(boolean rushDelivery) {
        this.rushDelivery = rushDelivery;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Media media = (Media) obj;
        return id == media.id;
    }
}
