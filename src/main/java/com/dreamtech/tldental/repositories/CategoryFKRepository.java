package com.dreamtech.tldental.repositories;

import com.dreamtech.tldental.models.CategoryFK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryFKRepository extends JpaRepository<CategoryFK, String>  {
    @Query("SELECT n FROM CategoryFK n WHERE (n.companyId.id LIKE %:companyId%) AND (:cate1Id LIKE '' OR n.cate1Id.id LIKE %:cate1Id%) AND (:cate2Id LIKE '' OR n.cate2Id.id LIKE %:cate2Id%)")
    List<CategoryFK> getCategoryFKByAll(@Param("companyId") String companyId, @Param("cate1Id") String cate1Id, @Param("cate2Id") String cate2Id);

    @Query("SELECT n FROM CategoryFK n WHERE n.companyId.id LIKE :companyId AND n.cate1Id.id LIKE :cate1Id AND n.cate2Id.id IS NULL")
    CategoryFK getCategoryFKCate1(@Param("companyId") String companyId, @Param("cate1Id") String cate1Id);

    @Query("SELECT n FROM CategoryFK n WHERE n.companyId.id LIKE :companyId AND n.cate1Id.id LIKE :cate1Id AND n.cate2Id.id LIKE :cate2Id")
    CategoryFK getCategoryFKCate2(@Param("companyId") String companyId, @Param("cate1Id") String cate1Id, @Param("cate2Id") String cate2Id);

    // Get all category 1 (having or no having category 2)
    @Query("SELECT n FROM CategoryFK n WHERE n.companyId.id LIKE :companyId AND n.cate1Id.id LIKE :cate1Id")
    List<CategoryFK> getCategoryFKCate1OrCate2(@Param("companyId") String companyId, @Param("cate1Id") String cate1Id);
}
