package com.dreamtech.tldental.repositories;

import com.dreamtech.tldental.models.Tags;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagsRepository extends JpaRepository<Tags, String> {
    List<Tags> findByName(String name);
}
