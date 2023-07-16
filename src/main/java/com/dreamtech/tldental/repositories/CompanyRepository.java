package com.dreamtech.tldental.repositories;

import com.dreamtech.tldental.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, String> {
    List<Company> findByName(String name);
    Company findBySlug(String slug);

    @Query("SELECT n FROM Company n WHERE n.highlight <> 0")
    List<Company> findHighlightCompany();
}
