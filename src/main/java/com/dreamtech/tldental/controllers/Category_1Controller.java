package com.dreamtech.tldental.controllers;

import com.dreamtech.tldental.models.*;
import com.dreamtech.tldental.repositories.CategoryFKRepository;
import com.dreamtech.tldental.repositories.Category_1Repository;
import com.dreamtech.tldental.repositories.CompanyRepository;
import com.dreamtech.tldental.services.IStorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cate1")
public class Category_1Controller {
    @Autowired
    private Category_1Repository repository;
    @Autowired
    private IStorageService storageService;

    // GET ALL WITH FILTER
    @GetMapping("")
    public ResponseEntity<ResponseObject> getAll(@RequestParam(required = false) boolean highlight) {
        List<Category_1> data;
        if (highlight) {
            data = repository.findHighlightCompany();
        } else {
            data = repository.findAll();
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query category level 1 successfully", data)
        );
    }

    // GET DETAIL
    @GetMapping("/{slug}")
    public ResponseEntity<ResponseObject> getDetail(@PathVariable String slug) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query company successfully", repository.findBySlug(slug))
        );
    }

    // CREATE CATEGORY LEVEL 1
    @PostMapping("")
    public ResponseEntity<ResponseObject> createCate1(@RequestParam ("data") String data,
                                                      @RequestPart("img") MultipartFile img) {
        try {
            // Convert String to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            Category_1 cate1Data = objectMapper.readValue(data, Category_1.class);

            // Check existed item
            List<Category_1> foundTags = repository.findByTitle(cate1Data.getTitle().trim());
            if (foundTags.size() > 0) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "Category's name already taken", "")
                );
            }

            // Check name has "/" or "\"
            if (cate1Data.getTitle().contains("/") || cate1Data.getTitle().contains("/")) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "Company's name should not have /", "")
                );
            }

            // Upload image to cloudinary
            String mainImgFileName = storageService.storeFile(img);
            cate1Data.setImg(mainImgFileName);
            cate1Data.setTitle(cate1Data.getTitle().trim());
            // Save to DB
            Category_1 resData = repository.save(cate1Data);


            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Insert company successfully", resData)
            );
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("failed", exception.getMessage(), "")
            );
        }
    }

    // UPDATE
    @PatchMapping("/{id}")
    ResponseEntity<ResponseObject> updateCate1(@PathVariable String id,
                                              @RequestPart(value = "img", required = false) MultipartFile img,
                                              @RequestParam ("data") String data) throws JsonProcessingException {
        // Convert String to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        Category_1 cate1Data = objectMapper.readValue(data, Category_1.class);

        Optional<Category_1> foundCate1 = repository.findById(id);
        if (foundCate1.isPresent()) {
            String oldUrlLogo = foundCate1.get().getImg();
            cate1Data.setCreateAt(foundCate1.get().getCreateAt());
            cate1Data.setImg(oldUrlLogo);
            // Copy new data
            BeanUtils.copyProperties(cate1Data, foundCate1.get());

            // Update img
            if (img != null && img.getSize() !=0) {
                storageService.deleteFile(oldUrlLogo);
                // Upload image to cloudinary
                String mainImgFileName = storageService.storeFile(img);
                foundCate1.get().setImg(mainImgFileName);
            }

            Category_1 resNews = repository.save(foundCate1.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update company successfully", resNews)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can not find company with id = "+cate1Data.getId(), "")
            );
        }
    }


    // UPDATE HIGHLIGHT CATEGORY LEVEL 1
    @PatchMapping("/highlight")
    ResponseEntity<ResponseObject> updateHighlight(@RequestBody ArrayList<Map<String, String>> data) {
        ArrayList<Object> res = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Optional<Category_1> foundCate1 = Optional.ofNullable(
                    repository.findBySlug(data.get(i).getOrDefault("slug", "none")));

            if (foundCate1.isPresent()) {
                Category_1 existingCate1 = foundCate1.get();
                existingCate1.setHighlight(Integer.parseInt(data.get(i).getOrDefault("highlight", "0")));

                Category_1 savedProduct = repository.save(existingCate1);
                res.add(savedProduct);
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Updated highlight category level 1 successfully", res)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteCompany(@PathVariable String id) {
        try {
            Optional<Category_1> foundCate1 = repository.findById(id);

            if (foundCate1.isPresent()) {
                storageService.deleteFile(foundCate1.get().getImg());

                repository.deleteById(id);

                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Deleted category level 1 successfully", foundCate1)
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
