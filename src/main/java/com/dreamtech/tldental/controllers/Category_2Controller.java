package com.dreamtech.tldental.controllers;

import com.dreamtech.tldental.models.Category_2;
import com.dreamtech.tldental.models.ResponseObject;
import com.dreamtech.tldental.repositories.Category_2Repository;
import com.dreamtech.tldental.services.IStorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cate2")
public class Category_2Controller {
    @Autowired
    private Category_2Repository repository;
    @Autowired
    private IStorageService storageService;

    // GET ALL WITH FILTER
    @GetMapping("")
    public ResponseEntity<ResponseObject> getAll() {
        List<Category_2> data = repository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query category level 2 successfully", data)
        );
    }

    // GET DETAIL
    @GetMapping("/{slug}")
    public ResponseEntity<ResponseObject> getDetail(@PathVariable String slug) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query category successfully", repository.findBySlug(slug))
        );
    }

    // CREATE CATEGORY LEVEL 2
    @PostMapping("")
    public ResponseEntity<ResponseObject> createCate2(@RequestBody Category_2 data) {
        try {
            // Check existed item
            List<Category_2> foundCate2 = repository.findByTitle(data.getTitle().trim());
            if (foundCate2.size() > 0) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "Category's name already taken", "")
                );
            }

            // Check name has "/" or "\"
            if (data.getTitle().contains("/") || data.getTitle().contains("/")) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "Company's name should not have /", "")
                );
            }

            // Upload image to cloudinary
            data.setTitle(data.getTitle().trim());

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Insert company successfully", repository.save(data))
            );
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("failed", exception.getMessage(), "")
            );
        }
    }

    // UPDATE
    @PatchMapping("/{id}")
    ResponseEntity<ResponseObject> updateCate2(@PathVariable String id,
                                               @RequestBody Category_2 data) throws JsonProcessingException {
         Optional<Category_2> foundCate2 = repository.findById(id);
        if (foundCate2.isPresent()) {
            data.setCreateAt(foundCate2.get().getCreateAt());
            // Copy new data
            BeanUtils.copyProperties(data, foundCate2.get());

            Category_2 resCate2 = repository.save(foundCate2.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update company successfully", resCate2)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can not find company with id = "+data.getId(), "")
            );
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteCompany(@PathVariable String id) {
        try {
            Optional<Category_2> foundCate2 = repository.findById(id);

            if (foundCate2.isPresent()) {
                repository.deleteById(id);

                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Deleted category level 2 successfully", foundCate2)
                );
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can not find category with id = "+id, "")
            );
        } catch (Exception exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", exception.getMessage(), "")
            );
        }
    }
}
