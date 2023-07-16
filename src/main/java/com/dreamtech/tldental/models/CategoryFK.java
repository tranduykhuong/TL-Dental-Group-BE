package com.dreamtech.tldental.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="CategoryFK")
public class CategoryFK {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company companyId;

    @ManyToOne
    @JoinColumn(name = "cate1_id", nullable = true)
    private Category_1 cate1Id;

    @ManyToOne
    @JoinColumn(name = "cate2_id", nullable = true)
    private Category_2 cate2Id;

    @JsonIgnore
    @OneToMany(mappedBy = "fkCategory")
    private List<Product> products;

    public CategoryFK() {
    }

    public CategoryFK(Company companyId, Category_1 cate1Id, Category_2 cate2Id) {
        this.companyId = companyId;
        this.cate1Id = cate1Id;
        this.cate2Id = cate2Id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Company getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Company companyId) {
        this.companyId = companyId;
    }

    public Category_1 getCate1Id() {
        return cate1Id;
    }

    public void setCate1Id(Category_1 cate1Id) {
        this.cate1Id = cate1Id;
    }

    public Category_2 getCate2Id() {
        return cate2Id;
    }

    public void setCate2Id(Category_2 cate2Id) {
        this.cate2Id = cate2Id;
    }
}
