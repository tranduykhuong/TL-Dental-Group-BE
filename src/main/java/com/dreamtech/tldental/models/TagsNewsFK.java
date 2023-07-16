package com.dreamtech.tldental.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="TagsNewsFK")
public class TagsNewsFK {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "fk_news", nullable = false)
    private String fkNews;

    @Column(name = "fk_tags", nullable = false)
    private String fkTags;

    public TagsNewsFK() {
    }

    public TagsNewsFK(String fkNews, String fkTags) {
        this.fkNews = fkNews;
        this.fkTags = fkTags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFkNews() {
        return fkNews;
    }

    public void setFkNews(String fkNews) {
        this.fkNews = fkNews;
    }

    public String getFkTags() {
        return fkTags;
    }

    public void setFkTags(String fkTags) {
        this.fkTags = fkTags;
    }
}
