package com.dreamtech.tldental.controllers;

import java.util.Arrays;
import java.util.Optional;

import javax.swing.text.html.Option;

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
import com.dreamtech.tldental.models.HomeSection1;
import com.dreamtech.tldental.models.ResponseObject;
import com.dreamtech.tldental.models.Review;
import com.dreamtech.tldental.repositories.ContentPageRepository;
import com.dreamtech.tldental.repositories.ReviewRepository;
import com.dreamtech.tldental.services.IStorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController 
@RequestMapping("/api/v1/home")
public class HomePageController {
    @Autowired
    private ContentPageRepository contentPageRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private IStorageService storageService;

    @GetMapping("/header")
    public ResponseEntity<ResponseObject> getHomeHeader() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get Header Successfully", contentPageRepository.findHomePageByTypeName("home::header"))
        );
    }

    @PostMapping(value="/header")
    public ResponseEntity<ResponseObject> addHomeHeader(@RequestBody ContentPage entity) {
        
        Optional<ContentPage> foundContentPage = contentPageRepository.findHomePageByTypeName("home::header");

        if (foundContentPage.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Type name already taken", "")
            );
        }

        entity.setType("home::header");

        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Add Header Successfully", contentPageRepository.save(entity))
        );
    }

    @PatchMapping(value="/header")
    public ResponseEntity<ResponseObject> updateContentPage(@RequestBody ContentPage entity) { 
        Optional<ContentPage> foundContentPage = contentPageRepository.findById(entity.getId());

        if (foundContentPage.isPresent()) {
            ContentPage currentContentPage = foundContentPage.get();

            if (currentContentPage.getType().equals("home::header")) {

                BeanUtils.copyProperties(entity, currentContentPage);

                currentContentPage.setType("home::header");

                return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update Content Page Successfully", contentPageRepository.save(currentContentPage))
                );
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            new ResponseObject("failed", "Cannot found your data", "")
        );
    }

    // SECTION 1

    @GetMapping("/section1")
    ResponseEntity<ResponseObject> getSection1() {
        Optional<ContentPage> section1Heading = contentPageRepository.findHomePageByTypeName("home::section1_heading");
        Optional<ContentPage> section1SubItem1 = contentPageRepository.findHomePageByTypeName("home::section1_subitem1");
        Optional<ContentPage> section1SubItem2 = contentPageRepository.findHomePageByTypeName("home::section1_subitem2");
        Optional<ContentPage> section1SubItem3 = contentPageRepository.findHomePageByTypeName("home::section1_subitem3");

        if (section1Heading.isPresent() && section1SubItem1.isPresent() && section1SubItem2.isPresent() && section1SubItem3.isPresent()) {
            HomeSection1 homeSection1 = new HomeSection1(section1Heading.get(), section1SubItem1.get(), section1SubItem2.get(), section1SubItem3.get());
            
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get Section 1 Successfully", homeSection1)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            new ResponseObject("failed", "Cannot found your data", "")
        );
    }    

    @PostMapping(value="/section1")
    public ResponseEntity<ResponseObject> addSection1(@RequestParam("image") MultipartFile image, @RequestParam ("data") String data) throws JsonMappingException, JsonProcessingException {

        Optional<ContentPage> Section1Heading = contentPageRepository.findHomePageByTypeName("home::section1_heading");
        Optional<ContentPage> Section1SubItem1 = contentPageRepository.findHomePageByTypeName("home::section1_subitem1");
        Optional<ContentPage> Section1SubItem2 = contentPageRepository.findHomePageByTypeName("home::section1_subitem2");
        Optional<ContentPage> Section1SubItem3 = contentPageRepository.findHomePageByTypeName("home::section1_subitem3");
        
        if (Section1Heading.isPresent() && Section1SubItem1.isPresent() && Section1SubItem2.isPresent() && Section1SubItem3.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Type name already taken", "")
            );
        }

        ObjectMapper objectMapper = new ObjectMapper();
        HomeSection1 entity = objectMapper.readValue(data, HomeSection1.class);

        entity.getHeading().setType("home::section1_heading");
        entity.getSubItem1().setType("home::section1_subitem1");
        entity.getSubItem2().setType("home::section1_subitem2");
        entity.getSubItem3().setType("home::section1_subitem3");

        String imageFile = storageService.storeFile(image);

        entity.getHeading().setImage(imageFile);

        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Add Header Successfully", contentPageRepository.saveAll(Arrays.asList(entity.getHeading(), entity.getSubItem1(), entity.getSubItem2(), entity.getSubItem3())))
        );
    }

    @PatchMapping(value="/section1")
    public ResponseEntity<ResponseObject> updateSection1(@RequestParam(name = "image", required = false) MultipartFile image, @RequestParam("data") String data) throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        HomeSection1 entity = objectMapper.readValue(data, HomeSection1.class);
        
        Optional<ContentPage> foundSection1Heading = contentPageRepository.findById(entity.getHeading().getId());
        Optional<ContentPage> foundSection1SubItem1 = contentPageRepository.findById(entity.getSubItem1().getId());
        Optional<ContentPage> foundSection1SubItem2 = contentPageRepository.findById(entity.getSubItem2().getId());
        Optional<ContentPage> foundSection1SubItem3 = contentPageRepository.findById(entity.getSubItem3().getId());

        if (foundSection1Heading.isPresent() && foundSection1SubItem1.isPresent() && foundSection1SubItem2.isPresent() && foundSection1SubItem3.isPresent()) {
            ContentPage section1Heading = foundSection1Heading.get();
            ContentPage section1SubItem1 = foundSection1SubItem1.get();
            ContentPage section1SubItem2 = foundSection1SubItem2.get();
            ContentPage section1SubItem3 = foundSection1SubItem3.get();
            
            if (section1Heading.getType().equals("home::section1_heading") 
                && section1SubItem1.getType().equals("home::section1_subitem1") 
                && section1SubItem2.getType().equals("home::section1_subitem2") 
                && section1SubItem3.getType().equals("home::section1_subitem3")) {

                BeanUtils.copyProperties(entity.getHeading(), section1Heading);
                BeanUtils.copyProperties(entity.getSubItem1(), section1SubItem1);
                BeanUtils.copyProperties(entity.getSubItem2(), section1SubItem2);
                BeanUtils.copyProperties(entity.getSubItem3(), section1SubItem3);

                section1Heading.setType("home::section1_heading");
                section1SubItem1.setType("home::section1_subitem1");
                section1SubItem2.setType("home::section1_subitem2");
                section1SubItem3.setType("home::section1_subitem3");

                if (image != null) {
                    String imageFile = storageService.storeFile(image);
                    section1Heading.setImage(imageFile);
                }

                return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update Content Page Successfully", contentPageRepository.saveAll(Arrays.asList(section1Heading, section1SubItem1, section1SubItem2, section1SubItem3)))
                );
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            new ResponseObject("failed", "Cannot found your data", "")
        );
    }

    @GetMapping(value="/section2")
    public ResponseEntity<ResponseObject> getSection2() {
        Optional<ContentPage> section2 = contentPageRepository.findHomePageByTypeName("home::section2");

        if (section2.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get Section 2 Successfully", section2.get())
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            new ResponseObject("failed", "Cannot found your data", "")
        );
    }

    @PostMapping(value="/section2")
    public ResponseEntity<ResponseObject> addSection2(@RequestParam("data") String data, @RequestParam("image") MultipartFile image) throws JsonMappingException, JsonProcessingException {
        Optional<ContentPage> section2 = contentPageRepository.findHomePageByTypeName("home::section2");

        if (section2.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("failed", "Type name already taken", "")
            );
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ContentPage entity = objectMapper.readValue(data, ContentPage.class);

        String imageFile = storageService.storeFile(image);

        entity.setImage(imageFile);
        entity.setType("home::section2");
        
        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Add section 2 successfully", contentPageRepository.save(entity))
        );
    }

    @PatchMapping(value="/section2")
    public ResponseEntity<ResponseObject> updateSection2(@RequestParam("data") String data, @RequestParam(name = "image", required = false) MultipartFile image) throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ContentPage entity = objectMapper.readValue(data, ContentPage.class);
        
        Optional<ContentPage> foundSection2 = contentPageRepository.findById(entity.getId());

        if (foundSection2.isPresent()) {

            ContentPage section2 = foundSection2.get();

            if (section2.getType().equals("home::section2")) {

                BeanUtils.copyProperties(entity, section2);

                if (image != null) {
                    String imageFile = storageService.storeFile(image);
                    section2.setImage(imageFile);
                }
                
                section2.setType("home::section2");
                
                return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update section 2 successfully", contentPageRepository.save(section2))
                );
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            new ResponseObject("failed", "Cannot found your data", "")
        );
    }

    @GetMapping(value="/section3")
    public ResponseEntity<ResponseObject> getSection3() {
        Optional<ContentPage> section3 = contentPageRepository.findHomePageByTypeName("home::section3");

        if (section3.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get Section 3 Successfully", section3.get())
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            new ResponseObject("failed", "Cannot found your data", "")
        );
    }

    @PostMapping(value="/section3")
    public ResponseEntity<ResponseObject> addSection3(@RequestParam("data") String data, @RequestParam("image") MultipartFile image) throws JsonMappingException, JsonProcessingException {
        Optional<ContentPage> section3 = contentPageRepository.findHomePageByTypeName("home::section3");

        if (section3.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("failed", "Type name already taken", "")
            );
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ContentPage entity = objectMapper.readValue(data, ContentPage.class);

        String imageFile = storageService.storeFile(image);

        entity.setImage(imageFile);
        entity.setType("home::section3");
        
        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Add section 3 successfully", contentPageRepository.save(entity))
        );
    }

    @PatchMapping(value="/section3")
    public ResponseEntity<ResponseObject> updateSection3(@RequestParam("data") String data, @RequestParam(name = "image", required = false) MultipartFile image) throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ContentPage entity = objectMapper.readValue(data, ContentPage.class);
        
        Optional<ContentPage> foundSection3 = contentPageRepository.findById(entity.getId());

        if (foundSection3.isPresent()) {

            ContentPage section3 = foundSection3.get();

            if (section3.getType().equals("home::section3")) {
                BeanUtils.copyProperties(entity, section3);

                if (image != null) {
                    String imageFile = storageService.storeFile(image);
                    section3.setImage(imageFile);
                }
                
                section3.setType("home::section3");
                
                return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update section 3 successfully", contentPageRepository.save(section3))
                );
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            new ResponseObject("failed", "Cannot found your data", "")
        );
    }

    @GetMapping(value="/reviews") ResponseEntity<ResponseObject> getReviews() {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Get Reviews Successfully", reviewRepository.findAll())
        );
    }

    @PostMapping(value="/reviews") ResponseEntity<ResponseObject> addReviews(@RequestParam("data") String data, @RequestParam("image") MultipartFile image) throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Review entity = objectMapper.readValue(data, Review.class);

        String imageFile = storageService.storeFile(image);

        entity.setImage(imageFile);
        
        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Add review successfully", reviewRepository.save(entity))
        );
    }

    @PatchMapping(value="/reviews")
    public ResponseEntity<ResponseObject> updateReviews(@RequestParam("data") String data, @RequestParam(name = "image", required = false) MultipartFile image) throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Review entity = objectMapper.readValue(data, Review.class);
        
        Optional<Review> foundReview = reviewRepository.findById(entity.getId());

        if (foundReview.isPresent()) {

            Review review = foundReview.get();

            BeanUtils.copyProperties(entity, review);

            if (image != null) {
                String imageFile = storageService.storeFile(image);
                review.setImage(imageFile);
            }
            
            
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update review successfully", reviewRepository.save(review))
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            new ResponseObject("failed", "Cannot found your data", "")
        );
    }

    @DeleteMapping(value = "/reviews/{id}") ResponseEntity<ResponseObject> deleteReview(@PathVariable("id") String id) {
        Optional<Review> foundReview = reviewRepository.findById(id);

        if (foundReview.isPresent()) {
            reviewRepository.deleteById(id);

            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Delete review successfully", "")
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            new ResponseObject("failed", "Cannot found your data", "")
        );
    }
}