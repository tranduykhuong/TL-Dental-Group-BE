package com.dreamtech.tldental.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dreamtech.tldental.models.Review;

public interface ReviewRepository extends JpaRepository<Review, String> {
    
}
