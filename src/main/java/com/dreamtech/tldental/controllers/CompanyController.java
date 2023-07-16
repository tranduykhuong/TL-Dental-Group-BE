package com.dreamtech.tldental.controllers;

import com.dreamtech.tldental.models.Company;
import com.dreamtech.tldental.models.ResponseObject;
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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/company")
public class CompanyController {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private IStorageService storageService;

    // GET ALL WITH FILTER
    @GetMapping("")
    public ResponseEntity<ResponseObject> getAll(@RequestParam(required = false) boolean highlight) {
        List <Company> data;
        if (highlight) {
            data = companyRepository.findHighlightCompany();
        } else {
            data = companyRepository.findAll();
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query company successfully", data)
        );
    }

    // GET DETAIL
    @GetMapping("/{slug}")
    public ResponseEntity<ResponseObject> getDetail(@PathVariable String slug) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query company successfully", companyRepository.findBySlug(slug))
        );
    }

    // CREATE COMPANY
    @PostMapping("")
    public ResponseEntity<ResponseObject> createCompany(@RequestParam ("data") String data,
                                                        @RequestPart("logo") MultipartFile logo) {
        try {
            // Convert String to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            Company companyData = objectMapper.readValue(data, Company.class);

            // Check existed item
            List<Company> foundTags = companyRepository.findByName(companyData.getName().trim());
            if (foundTags.size() > 0) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "Company's name already taken", "")
                );
            }

            // Check name has "/" or "\"
            if (companyData.getName().contains("/") || companyData.getName().contains("/")) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "Company's name should not have /", "")
                );
            }

            // Upload image to cloudinary
            String mainImgFileName = storageService.storeFile(logo);
            companyData.setLogo(mainImgFileName);
            companyData.setName(companyData.getName().trim());

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Insert company successfully", companyRepository.save(companyData))
            );
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("failed", exception.getMessage(), "")
            );
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteCompany(@PathVariable String id) {
        try {
            Optional<Company> foundCompany = companyRepository.findById(id);

            if (foundCompany.isPresent()) {
                storageService.deleteFile(foundCompany.get().getLogo());

                companyRepository.deleteById(id);

                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Deleted company successfully", foundCompany)
                );
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can not find company with id = "+id, "")
            );
        } catch (Exception exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", exception.getMessage(), "")
            );
        }
    }

    // UPDATE
    @PatchMapping("/{id}")
    ResponseEntity<ResponseObject> updateCompany(@PathVariable String id,
                                              @RequestPart(value = "logo", required = false) MultipartFile logo,
                                              @RequestParam ("data") String data) throws JsonProcessingException {
        // Convert String to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        Company companyData = objectMapper.readValue(data, Company.class);

        Optional<Company> foundCompany = companyRepository.findById(id);
        if (foundCompany.isPresent()) {
            String oldUrlLogo = foundCompany.get().getLogo();
            companyData.setCreateAt(foundCompany.get().getCreateAt());
            companyData.setLogo(oldUrlLogo);
            // Copy new data
            BeanUtils.copyProperties(companyData, foundCompany.get());

            // Update img
            if (logo != null && logo.getSize() !=0) {
                storageService.deleteFile(oldUrlLogo);
                // Upload image to cloudinary
                String mainImgFileName = storageService.storeFile(logo);
                foundCompany.get().setLogo(mainImgFileName);
            }


            Company resNews = companyRepository.save(foundCompany.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update company successfully", resNews)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can not find company with id = "+companyData.getId(), "")
            );
        }
    }
}
