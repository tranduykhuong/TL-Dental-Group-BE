package com.dreamtech.tldental.controllers;

import com.dreamtech.tldental.models.*;
import com.dreamtech.tldental.repositories.CategoryFKRepository;
import com.dreamtech.tldental.repositories.Category_1Repository;
import com.dreamtech.tldental.repositories.Category_2Repository;
import com.dreamtech.tldental.repositories.CompanyRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cate")
public class CategoryFKController {
    @Autowired
    private CategoryFKRepository categoryFKRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private Category_1Repository category1Repository;
    @Autowired
    private Category_2Repository category2Repository;



    // GET DETAIL
    @GetMapping("")
    public ResponseEntity<ResponseObject> getAll(@RequestBody CategoryData cateData) {
        System.out.println(cateData.getCompanyId());
        System.out.println(cateData.getCate1Id());
        System.out.println(cateData.getCate2Id());
        List<CategoryFK> cateExist = categoryFKRepository.getCategoryFKByAll(cateData.getCompanyId(), cateData.getCate1Id(), cateData.getCate2Id());

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query category successfully", cateExist)
        );
    }

    // GET DETAIL
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getDetail(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query category successfully", categoryFKRepository.findById(id))
        );
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> createCategory(@RequestBody CategoryData cateData) throws IOException {
        try {
            CategoryFK cateExistCate1 = categoryFKRepository.getCategoryFKCate1(cateData.getCompanyId(), cateData.getCate1Id());
            CategoryFK cateExistCate2 = categoryFKRepository.getCategoryFKCate2(cateData.getCompanyId(), cateData.getCate1Id(), cateData.getCate2Id());

            Company companyId = null;
            Category_1 cate1Id = null;
            Category_2 cate2Id = null;
            CategoryFK resCategory = null;

            if (cateData.getCate2Id().equals("")) {
                // Create cate1
                if (cateExistCate1 != null) {
                    return ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject("ok", "Category was created previously", cateExistCate1)
                    );
                } else {
                    // Create new
                    companyId = companyRepository.findById(cateData.getCompanyId()).orElseGet(null);
                    cate1Id = category1Repository.findById(cateData.getCate1Id()).orElseGet(null);
                    CategoryFK category = new CategoryFK(companyId, cate1Id, null);
                    resCategory = categoryFKRepository.save(category);
                }
            } else {
                // Create cate2
                if (cateExistCate2 != null) {
                    return ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject("ok", "Category was created previously", cateExistCate2)
                    );
                } else {
                    if (cateExistCate1 != null) {
                        cate2Id = category2Repository.findById(cateData.getCate2Id()).orElseGet(null);
                        cateExistCate1.setCate2Id(cate2Id);
                        resCategory = categoryFKRepository.save(cateExistCate1);
                    } else {
                        // Create new
                        companyId = companyRepository.findById(cateData.getCompanyId()).orElseGet(null);
                        cate1Id = category1Repository.findById(cateData.getCate1Id()).orElseGet(null);
                        cate2Id = category2Repository.findById(cateData.getCate2Id()).orElseGet(null);
                        CategoryFK category = new CategoryFK(companyId, cate1Id, cate2Id);
                        resCategory = categoryFKRepository.save(category);
                    }
                }
            }

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Insert category successfully", resCategory)
            );
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("failed", exception.getMessage(), "")
            );
        }
    }

    // Delete detail
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteCategoryFK(@PathVariable String id) {
        try {
            categoryFKRepository.deleteById(id);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Deleted company successfully", "")
            );
        } catch (Exception exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", exception.getMessage(), "")
            );
        }
    }

    // DELETE
    @DeleteMapping("")
    public ResponseEntity<ResponseObject> deleteCategory(@RequestBody CategoryData cateData) throws IOException {
        try {
            if (cateData.getCate2Id().equals("")) {
                // Delete cate1
                List<CategoryFK> categoryFKList = categoryFKRepository.getCategoryFKCate1OrCate2(cateData.getCompanyId(), cateData.getCate1Id());
                for (int i = 0; i < categoryFKList.size(); i++) {
                    categoryFKRepository.deleteById(categoryFKList.get(i).getId());
                }

                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Delete category successfully", categoryFKList)
                );
            } else {
                // Delete cate2
                CategoryFK cateExistCate2 = categoryFKRepository.getCategoryFKCate2(cateData.getCompanyId(), cateData.getCate1Id(), cateData.getCate2Id());
                categoryFKRepository.deleteById(cateExistCate2.getId());

                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Delete category successfully", cateExistCate2)
                );
            }
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("failed", exception.getMessage(), "")
            );
        }
    }

    private static class CategoryData {
        private String companyId;
        private String cate1Id;
        private String cate2Id;

        public CategoryData() {
        }

        public CategoryData(String companyId, String cate1Id, String cate2Id) {
            this.companyId = companyId;
            this.cate1Id = cate1Id;
            this.cate2Id = cate2Id;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public String getCate1Id() {
            return cate1Id;
        }

        public void setCate1Id(String cate1Id) {
            this.cate1Id = cate1Id;
        }

        public String getCate2Id() {
            return cate2Id;
        }

        public void setCate2Id(String cate2Id) {
            this.cate2Id = cate2Id;
        }
    }
}
