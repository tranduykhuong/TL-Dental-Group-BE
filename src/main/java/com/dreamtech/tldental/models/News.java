package com.dreamtech.tldental.models;

import com.dreamtech.tldental.utils.Utils;
import jakarta.persistence.*;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.Locale;

@Entity
@Table(name="News")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 200, unique = true)
    private String title;

    @Column(nullable = false)
    private String img;

    @Column(unique = true)
    private String slug;

    @Column(length = 300)
    private String summary;

    @Column(length = 5000)
    private String detail;

    @Column(name = "detail_mobile", length = 5000)
    private String detailMobile;

    private int highlight;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @PrePersist
    protected void onCreate() {
        createAt = LocalDateTime.now();
        this.slug = Utils.generateSlug(title);
    }

    @PreUpdate
    protected void preUpdate() {
        this.slug = Utils.generateSlug(title);
    }

    public News() {
    }

    public News(String title, String img, String summary, String detail, String detailMobile, int highlight) {
        this.title = title;
        this.img = img;
        this.summary = summary;
        this.detail = detail;
        this.detailMobile = detailMobile;
        this.highlight = highlight;
    }

    @Override
    public String toString() {
        return "News{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", img='" + img + '\'' +
                ", slug='" + slug + '\'' +
                ", summary='" + summary + '\'' +
                ", detail='" + detail + '\'' +
                ", detailMobile='" + detailMobile + '\'' +
                ", highlight=" + highlight +
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDetailMobile() {
        return detailMobile;
    }

    public void setDetailMobile(String detailMobile) {
        this.detailMobile = detailMobile;
    }

    public int getHighlight() {
        return highlight;
    }

    public void setHighlight(int highlight) {
        this.highlight = highlight;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
}
