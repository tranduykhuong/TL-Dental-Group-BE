package com.dreamtech.tldental.controllers;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dreamtech.tldental.models.ContentPage;
import com.dreamtech.tldental.models.ResponseObject;
import com.dreamtech.tldental.repositories.ContentPageRepository;
import com.dreamtech.tldental.services.IStorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/v1/introduce")
public class IntroduceController {
    @Autowired
    private ContentPageRepository contentPageRepository;

    @Autowired
    private IStorageService storageService;

    @GetMapping("/header")
    public ResponseEntity<ResponseObject> getHeader() {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Get header successfully", contentPageRepository.findHomePageByTypeName("introduce::header"))
        );
    }

    @PostMapping("/header")
    public ResponseEntity<ResponseObject> addHeader(@RequestParam("image") MultipartFile image) {
        Optional<ContentPage> foundHeader = contentPageRepository.findHomePageByTypeName("introduce::header");
    
        if (foundHeader.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("failed", "Type name already taken", "")
            );
        }

        String imageFile = storageService.storeFile(image);
        
        ContentPage entity = new ContentPage(null, "Giới Thiệu", null, imageFile, null, "introduce::header");

        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Add header successfully", contentPageRepository.save(entity))
        );
    }

    @PatchMapping("/header")
    public ResponseEntity<ResponseObject> updateHeader(@RequestParam("data") String data, @RequestParam(name = "image", required = false) MultipartFile image) throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ContentPage entity = objectMapper.readValue(data, ContentPage.class);
        
        Optional<ContentPage> foundHeader = contentPageRepository.findById(entity.getId());
    
        if (foundHeader.isPresent()) {
            ContentPage header = foundHeader.get();

            if (header.getType().equals("introduce::header")) {
                BeanUtils.copyProperties(entity, header);

                if (image != null) 
                    header.setImage(storageService.storeFile(image));

                header.setType("introduce::header");

                return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update header successfully", contentPageRepository.save(header))
                );
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            new ResponseObject("failed", "Cannot found your data", "")
        );
    }

    @GetMapping("/letter")
    public ResponseEntity<ResponseObject> getLetter() {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Get letter successfully", contentPageRepository.findHomePageByTypeName("introduce::letter"))
        );
    }

    @PostMapping("/letter")
    public ResponseEntity<ResponseObject> addLetter(@RequestBody ContentPage entity) {
        Optional<ContentPage> foundLetter = contentPageRepository.findHomePageByTypeName("introduce::letter");
    
        if (foundLetter.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("failed", "Type name already taken", "")
            );
        }

        entity.setType("introduce::letter");

        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Add letter successfully", contentPageRepository.save(entity))
        );
    }

    @PatchMapping("/letter")
    public ResponseEntity<ResponseObject> updateLetter(@RequestBody ContentPage entity) {
        Optional<ContentPage> foundLetter = contentPageRepository.findById(entity.getId());
    
        if (foundLetter.isPresent()) {
            ContentPage letter = foundLetter.get();

            if (letter.getType().equals("introduce::letter")) {
                BeanUtils.copyProperties(entity, letter);

                letter.setType("introduce::letter");

                return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update letter successfully", contentPageRepository.save(letter))
                );
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            new ResponseObject("failed", "Cannot found your data", "")
        );
    }

    @GetMapping("/company-info")
    public ResponseEntity<ResponseObject> getCompanyInfor() {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Get company information successfully", contentPageRepository.findHomePageByTypeName("introduce::company-info"))
        );
    }

    @PostMapping("/company-info")
    public ResponseEntity<ResponseObject> addCompanyInfor(@RequestParam("data") String data, @RequestParam("image") MultipartFile image) throws JsonMappingException, JsonProcessingException {
        Optional<ContentPage> foundCompanyInfor = contentPageRepository.findHomePageByTypeName("introduce::company-info");
    
        if (foundCompanyInfor.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("failed", "Type name already taken", "")
            );
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ContentPage entity = objectMapper.readValue(data, ContentPage.class);

        String imageFile = storageService.storeFile(image);
        entity.setImage(imageFile);

        entity.setType("introduce::company-info");

        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Add company information successfully", contentPageRepository.save(entity))
        );
    }

    @PatchMapping("/company-info")
    public ResponseEntity<ResponseObject> updateCompanyInfor(@RequestParam("data") String data, @RequestParam(name = "image", required = false) MultipartFile image) throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ContentPage entity = objectMapper.readValue(data, ContentPage.class);
        
        Optional<ContentPage> foundedCompanyInfor = contentPageRepository.findById(entity.getId());
    
        if (foundedCompanyInfor.isPresent()) {
            ContentPage compInfo = foundedCompanyInfor.get();

            if (compInfo.getType().equals("introduce::company-info")) {
                BeanUtils.copyProperties(entity, compInfo);
                
                if (image != null) {
                    String imageFile = storageService.storeFile(image);
                    compInfo.setImage(imageFile);
                }
                
                compInfo.setType("introduce::company-info");

                return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update company information successfully", contentPageRepository.save(compInfo))
                );
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            new ResponseObject("failed", "Cannot found your data", "")
        );
    }

    @GetMapping("/section1")
    public ResponseEntity<ResponseObject> getSection1() {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Get section1 successfully", contentPageRepository.findAllByType("introduce::section1"))
        );
    }

    @PostMapping("/section1")
    public ResponseEntity<ResponseObject> addSection1(@RequestParam("data") String data, @RequestParam("image") MultipartFile image) throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ContentPage entity = objectMapper.readValue(data, ContentPage.class);

        String imageFile = storageService.storeFile(image);
        entity.setImage(imageFile);

        entity.setType("introduce::section1");

        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Add section1 successfully", contentPageRepository.save(entity))
        );
    }

    @PatchMapping("/section1")
    public ResponseEntity<ResponseObject> updateSection1(@RequestParam("data") String data, @RequestParam(name = "image", required = false) MultipartFile image) throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ContentPage entity = objectMapper.readValue(data, ContentPage.class);
        
        Optional<ContentPage> foundSection1 = contentPageRepository.findById(entity.getId());
    
        if (foundSection1.isPresent()) {
            ContentPage section1 = foundSection1.get();

            if (section1.getType().equals("introduce::section1")) {
                BeanUtils.copyProperties(entity, section1);
                
                if (image != null) {
                    String imageFile = storageService.storeFile(image);
                    section1.setImage(imageFile);
                }
                
                section1.setType("introduce::section1");

                return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update section1 successfully", contentPageRepository.save(section1))
                );
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            new ResponseObject("failed", "Cannot found your data", "")
        );
    }

    @DeleteMapping(value = "/section1/{id}") ResponseEntity<ResponseObject> deleteReview(@PathVariable("id") String id) {
        Optional<ContentPage> foundSection1 = contentPageRepository.findById(id);

        if (foundSection1.isPresent() && foundSection1.get().getType().equals("introduce::section1")) {
            contentPageRepository.deleteById(id);

            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Delete section1 successfully", "")
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            new ResponseObject("failed", "Cannot found your data", "")
        );
    }
}
