package com.dreamtech.tldental.models;

import com.dreamtech.tldental.utils.Utils;
import jakarta.persistence.*;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.Locale;

@Entity
@Table(name="Tags")
public class Tags {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 200, unique = true)
    private String name;

    @Column(unique = true)
    private String slug;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @PrePersist
    protected void onCreate() {
        createAt = LocalDateTime.now();
        this.slug = Utils.generateSlug(name);
    }

    @PreUpdate
    protected void preUpdate() {
        this.slug = Utils.generateSlug(name);
    }

    public Tags() {
    }

    public Tags(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "Tags{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                '}';
    }
}
