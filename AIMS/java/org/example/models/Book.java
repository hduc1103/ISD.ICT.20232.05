package org.example.models;

import java.util.Date;

public class Book extends Media {
    /**
     * Single responsibility:
     * The Book class has one responsibility: to store data specific to books.
     */
    private String author;
    private String coverType;
    private String publisher;
    private Date publishDate;
    private int numOfPages;
    private String language;
    private String bookCategory;

    public Book(int id, String name, double price, boolean rushDelivery, double weight, int available, String imageUrl, String category, 
                String author, String coverType, String publisher, Date publishDate, int numOfPages, String language, String bookCategory) {
        super(id, name, price, rushDelivery, weight, available, imageUrl, category);
        this.author = author;
        this.coverType = coverType;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.numOfPages = numOfPages;
        this.language = language;
        this.bookCategory = bookCategory;
    }

    // Getters and Setters
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoverType() {
        return coverType;
    }

    public void setCoverType(String coverType) {
        this.coverType = coverType;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public int getNumOfPages() {
        return numOfPages;
    }

    public void setNumOfPages(int numOfPages) {
        this.numOfPages = numOfPages;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }
}
