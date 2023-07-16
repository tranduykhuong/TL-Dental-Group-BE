package com.dreamtech.tldental.repositories;

import com.dreamtech.tldental.models.ContentPage;

import java.util.Optional;

import org.hibernate.mapping.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContentPageRepository extends JpaRepository<ContentPage, String> {
    @Query( "SELECT ct_pg FROM ContentPage ct_pg WHERE ct_pg.type = :type_name" )
    Optional<ContentPage> findHomePageByTypeName(@Param("type_name") String typeName);

    @Query( "SELECT ct_pg FROM ContentPage ct_pg WHERE ct_pg.type = :type_name" )
    Optional<ContentPage[]> findAllByType(@Param("type_name") String typeName);
}