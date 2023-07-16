package com.dreamtech.tldental.models;

import com.dreamtech.tldental.utils.Utils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="Company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @Column(nullable = false)
    private String logo;

    @Column(length = 500)
    private String description;

    private int highlight;

    @Column(unique = true)
    private String slug;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @JsonIgnore
    @OneToMany(mappedBy = "companyId")
    private List<CategoryFK> cateFKs;

    @PrePersist
    protected void onCreate() {
        createAt = LocalDateTime.now();
        this.slug = Utils.generateSlug(name);
    }

    @PreUpdate
    protected void preUpdate() {
        this.slug = Utils.generateSlug(name);
    }

    public Company() {
    }

    public Company(String name, String description, int highlight) {
        this.name = name;
        this.description = description;
        this.highlight = highlight;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", description='" + description + '\'' +
                ", highlight=" + highlight +
                ", slug='" + slug + '\'' +
                ", createAt=" + createAt +
                ", cateFKs=" + cateFKs +
                '}';
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

    public List<CategoryFK> getCateFKs() {
        return cateFKs;
    }

    public void setCateFKs(List<CategoryFK> cateFKs) {
        this.cateFKs = cateFKs;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }
}
