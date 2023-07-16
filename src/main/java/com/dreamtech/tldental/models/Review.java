package com.dreamtech.tldental.models;

import jakarta.persistence.*;

@Entity
@Table(name="Reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(length = 1000, unique = false)
    private String fullname;

    @Column(length = 500, unique = false)
    private String position;

    @Column(unique = false)
    private float rating;

    @Column(length = 10000, unique = true)
    private String content;

    @Column(length = 200, unique = false)
    private String image;

    public Review() {
    }

    public Review(String id, String fullname, String position, float rating, String content, String image) {
        this.id = id;
        this.fullname = fullname;
        this.position = position;
        this.rating = rating;
        this.content = content;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}