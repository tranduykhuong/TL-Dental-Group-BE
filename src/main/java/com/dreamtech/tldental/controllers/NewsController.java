package com.dreamtech.tldental.controllers;

import com.dreamtech.tldental.models.*;
import com.dreamtech.tldental.repositories.NewsRepository;
import com.dreamtech.tldental.repositories.TagsNewsFKRepository;
import com.dreamtech.tldental.services.IStorageService;
import com.dreamtech.tldental.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {
    @Autowired
    private NewsRepository repository;
    @Autowired
    private TagsNewsFKRepository fkTagsNewsRepository;
    @Autowired
    private IStorageService storageService;

    // GET ALL NEWS WITH FILTER
    @GetMapping("")
    ResponseEntity<ResponseObject> getFilter(@RequestParam(required = false, defaultValue = "12") String pageSize,
                                             @RequestParam(required = false, defaultValue = "0") String page,
                                             @RequestParam(required = false) List<String> filterTags,
                                             @RequestParam(required = false, defaultValue = "desc") String sort) {
        Sort.Direction sortDirection = sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sortByCreateAt = Sort.by(sortDirection, "createAt");

        List<Object[]> newsList;
        int total;
        if (filterTags == null) {
            newsList = repository.findFilteredNews(PageRequest
                    .of(Integer.parseInt(page), Integer.parseInt(pageSize), sortByCreateAt));
        } else {
            newsList = repository.findFilteredNewsByTags(filterTags, PageRequest
                    .of(Integer.parseInt(page), Integer.parseInt(pageSize), sortByCreateAt));
        }

        // Handle data
        List<Object> combinedList = handleDataNews(newsList);
        total = combinedList.size();

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query news successfully", new DataPageObject(total, page, pageSize, combinedList))
        );
    }

    @GetMapping("/total")
    ResponseEntity<ResponseObject> getTotal(@RequestParam(required = false) List<String> filterTags) {
        List<Object[]> newsList;
        if (filterTags == null) {
            newsList = repository.findFilteredNews(null);
        } else {
            newsList = repository.findFilteredNewsByTags(filterTags, null);
        }

        // Handle data
        List<Object> combinedList = handleDataNews(newsList);
        int total = combinedList.size();

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query total successfully", total)
        );
    }

    // GET NEWS WITH FILTER BY MONTH
    @GetMapping("/month")
    ResponseEntity<ResponseObject> getNewsByMonth(@RequestParam int month) {
        List<Object[]> foundNews = repository.findNewsByMonth(month);
        List<Object> data = handleDataNews(foundNews);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get news by month (" + month + ") successfully", data)
        );
    }

    // GET DETAIL NEWS
    @GetMapping("/{slug}")
    ResponseEntity<ResponseObject> getDetail(@PathVariable String slug) {
        Optional<News> foundNews = Optional.ofNullable(repository.findBySlug(slug));
        return foundNews.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Query news successfully", foundNews)
                ):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Can not find product with id = "+slug, "")
                );
    }

    // CREATE NEWS
    @PostMapping("")
    ResponseEntity<ResponseObject> createNews(@RequestPart("img") MultipartFile img,
                                              @RequestParam ("data") String data,
                                              @RequestParam ("tags") String tags) {
        try {
            // Convert String to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            News newsData = objectMapper.readValue(data, News.class);

            List<String> tagsId = Utils.convertStringToImages(tags);

            // Check existed item
            List<News> foundNews = repository.findByTitle(newsData.getTitle().trim());
            if (foundNews.size() > 0) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "News's name already taken", "")
                );
            }

            // Check name has "/" or "\"
            if (newsData.getTitle().contains("/") || newsData.getTitle().contains("/")) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "News's name should not have /", "")
                );
            }
            // Upload image to cloudinary
            String mainImgFileName = storageService.storeFile(img);
            newsData.setImg(mainImgFileName);
            newsData.setTitle(newsData.getTitle().trim());

            // Create News
            News resNews = repository.save(newsData);

            // Create FK_TAGS_NEWS
            for (int i = 0; i < tagsId.size(); i++) {
                fkTagsNewsRepository.save(new TagsNewsFK(resNews.getId(), tagsId.get(i)));
            }

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Insert news successfully", resNews)
            );
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("failed", exception.getMessage(), "")
            );
        }
    }

    // UPDATE NEWS
    @PatchMapping("/{id}")
    ResponseEntity<ResponseObject> updateNews(@PathVariable String id,
                                              @RequestPart(value = "img", required = false) MultipartFile img,
                                              @RequestParam("data") String data,
                                              @RequestParam("tags") String tags) throws JsonProcessingException {
        // Convert String to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        News newsData = objectMapper.readValue(data, News.class);

        List<String> tagsId = Utils.convertStringToImages(tags);

        Optional<News> foundNews = repository.findById(id);
        if (foundNews.isPresent()) {
            String oldUrlImg = foundNews.get().getImg();
            newsData.setCreateAt(foundNews.get().getCreateAt());
            // Copy new data
            BeanUtils.copyProperties(newsData, foundNews.get());

            // Update FK_NEWS_TAGS if it was changed
            if (tagsId.size() > 0) {
                fkTagsNewsRepository.deleteByFkNews(foundNews.get().getId());
                // Create FK_TAGS_NEWS
                for (int i = 0; i < tagsId.size(); i++) {
                    fkTagsNewsRepository.save(new TagsNewsFK(foundNews.get().getId(), tagsId.get(i)));
                }
            }

            // Update img
            if (img != null && img.getSize() != 0) {
                storageService.deleteFile(oldUrlImg);
                // Upload image to cloudinary
                String mainImgFileName = storageService.storeFile(img);
                foundNews.get().setImg(mainImgFileName);
            }

            News resNews = repository.save(foundNews.get());

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update news successfully", resNews)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can not find news with id = "+newsData.getId(), "")
            );
        }
    }

    // DELETE NEWS
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteNews(@PathVariable String id) {
        Optional<News> foundNews = repository.findById(id);

        if (foundNews.isPresent()) {
            storageService.deleteFile(foundNews.get().getImg());

            fkTagsNewsRepository.deleteByFkNews(foundNews.get().getId());

            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Deleted news successfully", foundNews)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can not find news with id = "+id, "")
        );
    }


    // HIGHLIGHT //
    // GET ALL HIGHLIGHT NEWS
    @GetMapping("/highlight")
    ResponseEntity<ResponseObject> getHighlightNews() {
        List<Object[]> foundNews = repository.findHighlightNews();
        List<Object> data = handleDataNews(foundNews);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get all highlight news successfully", data)
        );
    }

    // UPDATE HIGHLIGHT NEWS
    @PatchMapping("/highlight/{slug}")
    ResponseEntity<ResponseObject> updateHighlightNews(@PathVariable String slug,
                                                   @RequestParam int highlight) {
        Optional<News> foundNews = Optional.ofNullable(repository.findBySlug(slug));
        foundNews.get().setHighlight(highlight);
        System.out.println(highlight);
        return foundNews.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Update highlight news successfully", repository.save(foundNews.get()))
                ):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Can not find product with id = "+slug, "")
                );
    }



    private List<Object> handleDataNews(List<Object[]> srcList) {
        List<Object> combinedList = new ArrayList<>(); // Result
        List<Object> tempList = new ArrayList<>(); // Store tags of a news
        Map<String, Object> tempObj = new HashMap<String, Object>();

        for (Object[] result : srcList) {
            News news = (News) result[0];
            Tags tags = (Tags) result[1];

            if (tempList.isEmpty() || !((News) tempObj.get("news")).getId().equals(news.getId())) {
                if (!tempList.isEmpty()) {
                    // Check news has not tag
                    if (tempList.get(0) == null)
                        tempList.remove(0);
                    tempObj.put("tags", tempList);
                    combinedList.add(tempObj);
                    tempObj = new HashMap<>();
                }
                tempList = new ArrayList<>();
                tempObj.put("news", news);
            }

            tempList.add(tags);
        }
        if (tempObj.containsKey("news")) {
            if (tempList.get(0) == null)
                tempList.remove(0);
            tempObj.put("tags", tempList);
            combinedList.add(tempObj);
        }
        return combinedList;
    }

}
