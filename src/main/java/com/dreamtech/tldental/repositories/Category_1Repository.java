package com.dreamtech.tldental.repositories;

import com.dreamtech.tldental.models.Category_1;
import com.dreamtech.tldental.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Category_1Repository extends JpaRepository<Category_1, String>  {
    Category_1 findBySlug(String slug);
    List<Category_1> findByTitle(String title);

    @Query("SELECT n FROM Category_1 n WHERE n.highlight <> 0")
    List<Category_1> findHighlightCompany();
}
