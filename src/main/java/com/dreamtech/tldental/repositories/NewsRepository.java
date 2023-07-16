package com.dreamtech.tldental.repositories;

import com.dreamtech.tldental.models.News;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, String> {
    List<News> findByTitle(String title);
    News findBySlug(String name);

    @Query("SELECT n, t FROM News n JOIN TagsNewsFK tn ON tn.fkNews = n.id JOIN Tags t ON tn.fkTags = t.id WHERE n.id IN (SELECT n.id FROM News n JOIN TagsNewsFK tn ON tn.fkNews = n.id JOIN Tags t ON tn.fkTags = t.id WHERE t.slug IN (:filterTags))")
    List<Object[]> findFilteredNewsByTags(@Param("filterTags") List<String> filterTags, Pageable pageable);

    @Query("SELECT n, t FROM News n LEFT JOIN TagsNewsFK tn ON tn.fkNews = n.id LEFT JOIN Tags t ON tn.fkTags = t.id")
    List<Object[]> findFilteredNews(Pageable pageable);

    @Query("SELECT n, t FROM News n LEFT JOIN TagsNewsFK tn ON tn.fkNews = n.id LEFT JOIN Tags t ON tn.fkTags = t.id WHERE n.highlight <> 0")
    List<Object[]> findHighlightNews();

    @Query("SELECT n, t FROM News n LEFT JOIN TagsNewsFK tn ON tn.fkNews = n.id LEFT JOIN Tags t ON tn.fkTags = t.id WHERE EXTRACT(MONTH FROM n.createAt) = :month")
    List<Object[]> findNewsByMonth(@Param("month") int month);
}
