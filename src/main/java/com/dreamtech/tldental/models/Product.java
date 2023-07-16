package com.dreamtech.tldental.models;


import com.dreamtech.tldental.utils.Utils;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Entity
@Table(name="Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 200, unique = true)
    private String name;

    @Column(unique = true)
    private String slug;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private int price;

    @Column(name = "price_sale")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private int priceSale;

    @Column(length = 300)
    private String summary;

    @Column(length = 1000)
    private String description;

    @Column(name = "main_img", nullable = false)
    private String mainImg;

    @Column(length = 1000)
    private String imgs;
    private int highlight;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @ManyToOne
    @JoinColumn(name = "fk_category", nullable = false)
    private CategoryFK fkCategory;

    @PrePersist
    protected void onCreate() {
        createAt = LocalDateTime.now();
        this.slug = Utils.generateSlug(name);
    }

    @PreUpdate
    protected void preUpdate() {
        this.slug = Utils.generateSlug(name);
    }

    public Product() {
    }

    public Product(String name, int price, int priceSale, String summary, String description, CategoryFK categoryFK) {
        this.name = name;
        this.price = price;
        this.priceSale = priceSale;
        this.summary = summary;
        this.description = description;
        this.fkCategory = categoryFK;
    }

    public CategoryFK getFkCategory() {
        return fkCategory;
    }

    public void setFkCategory(CategoryFK fkCategory) {
        this.fkCategory = fkCategory;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPriceSale() {
        return priceSale;
    }

    public void setPriceSale(int priceSale) {
        this.priceSale = priceSale;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getMainImg() {
        return mainImg;
    }

    public void setMainImg(String mainImg) {
        this.mainImg = mainImg;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
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

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", priceSale=" + priceSale +
                ", summary='" + summary + '\'' +
                ", description='" + description + '\'' +
                ", slug='" + slug + '\'' +
                ", mainImg='" + mainImg + '\'' +
                ", imgs='" + imgs + '\'' +
                ", highlight=" + highlight +
                ", createAt=" + createAt +
                '}';
    }
}
