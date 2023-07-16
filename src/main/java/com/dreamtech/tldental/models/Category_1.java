package com.dreamtech.tldental.models;

import com.dreamtech.tldental.utils.Utils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="Category_1")
public class Category_1 {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false, length = 100, unique = true)
    private String title;

    private String img;

    private int highlight;

    @Column(unique = true)
    private String slug;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @JsonIgnore
    @OneToMany(mappedBy = "cate1Id")
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

    public Category_1() {
    }

    public Category_1(String title, int highlight) {
        this.title = title;
        this.highlight = highlight;
    }

    @Override
    public String toString() {
        return "Category_1{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", img='" + img + '\'' +
                ", highlight=" + highlight +
                ", slug='" + slug + '\'' +
                ", createAt=" + createAt +
                '}';
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getHighlight() {
        return highlight;
    }

    public void setHighlight(int highlight) {
        this.highlight = highlight;
    }

    public String getSlug() {
        return slug;
    }

    public List<CategoryFK> getCateFKs() {
        return cateFKs;
    }

    public void setCateFKs(List<CategoryFK> cateFKs) {
        this.cateFKs = cateFKs;
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
