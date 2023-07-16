package com.dreamtech.tldental.repositories;

import com.dreamtech.tldental.models.Category_2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Category_2Repository extends JpaRepository<Category_2, String>  {
    Category_2 findBySlug(String slug);
    List<Category_2> findByTitle(String title);
}
