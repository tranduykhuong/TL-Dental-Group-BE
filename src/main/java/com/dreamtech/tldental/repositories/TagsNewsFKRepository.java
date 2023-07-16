package com.dreamtech.tldental.repositories;

import com.dreamtech.tldental.models.Product;
import com.dreamtech.tldental.models.TagsNewsFK;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagsNewsFKRepository extends JpaRepository<TagsNewsFK, String>  {
    @Transactional
    void deleteByFkNews(String fkNews);

    @Transactional
    void deleteByFkTags(String fkTags);
}
