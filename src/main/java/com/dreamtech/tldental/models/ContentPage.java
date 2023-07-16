package com.dreamtech.tldental.models;

import jakarta.persistence.*;

@Entity
@Table(name="ContentPage")
public class ContentPage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(length = 200, unique = false)
    private String title;

    @Column(length = 1000000, unique = false)
    private String content;

    @Column(length = 200, unique = false)
    private String image;

    @Column(unique = true)
    private String slug;

    @Column(length = 200, unique = false)
    private String type;

    public ContentPage() {
    }

    public ContentPage(String id, String title, String content, String image, String slug, String type) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image = image;
        this.slug = slug;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}