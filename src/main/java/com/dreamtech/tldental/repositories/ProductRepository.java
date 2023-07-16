package com.dreamtech.tldental.repositories;

import com.dreamtech.tldental.models.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByName(String name);
    Product findBySlug(String name);

    @Query("SELECT p FROM Product p WHERE (:company IS NULL OR p.fkCategory.companyId.slug LIKE :company) AND (:cate1 IS NULL OR p.fkCategory.cate1Id.slug LIKE :cate1) AND (:cate2 IS NULL OR p.fkCategory.cate2Id.slug LIKE :cate2)")
    List<Object[]> findFilteredProducts(@Param("company") String company, @Param("cate1") String cate1, @Param("cate2") String cate2, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.highlight <> 0")
    List<Product> getAllHighlight();
}

