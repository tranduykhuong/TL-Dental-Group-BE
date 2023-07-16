package com.dreamtech.tldental.models;

import com.dreamtech.tldental.utils.Utils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="Category_2")
public class Category_2 {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false, length = 100, unique = true)
    private String title;

    @Column(unique = true)
    private String slug;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @JsonIgnore
    @OneToMany(mappedBy = "cate2Id")
    private List<CategoryFK> cateFKs;

    @PrePersist
    protected void onCreate() {
        createAt = LocalDateTime.now();
        this.slug = Utils.generateSlug(title);
    }

    @PreUpdate
    protected void preUpdate() {
        this.slug = Utils.generateSlug(title);
    }

    public Category_2() {
    }

    public Category_2(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Category_2{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", slug='" + slug + '\'' +
                ", createAt=" + createAt +
                ", cateFKs=" + cateFKs +
                '}';
    }

    public List<CategoryFK> getCateFKs() {
        return cateFKs;
    }

    public void setCateFKs(List<CategoryFK> cateFKs) {
        this.cateFKs = cateFKs;
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
}
