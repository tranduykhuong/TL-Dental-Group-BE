package com.dreamtech.tldental.controllers;

import com.dreamtech.tldental.models.News;
import com.dreamtech.tldental.models.ResponseObject;
import com.dreamtech.tldental.models.Tags;
import com.dreamtech.tldental.repositories.TagsNewsFKRepository;
import com.dreamtech.tldental.repositories.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tags")
public class TagsController {
    @Autowired
    private TagsRepository repository;
    @Autowired
    private TagsNewsFKRepository fkTagsNewsRepository;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query tags successfully", repository.findAll())
        );
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> createTag(@RequestBody Tags data) {
        try {
            // Check existed item
            List<Tags> foundTags = repository.findByName(data.getName().trim());
            if (foundTags.size() > 0) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "Tag's name already taken", "")
                );
            }

            // Check name has "/" or "\"
            if (data.getName().contains("/") || data.getName().contains("/")) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "Tag's name should not have /", "")
                );
            }

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Insert tag successfully", repository.save(data))
            );
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("failed", exception.getMessage(), "")
            );
        }
    }

    @PatchMapping("/{id}")
    ResponseEntity<ResponseObject> updateTag(@PathVariable String id, @RequestParam String name) {
        Optional<Tags> foundTags = repository.findById(id);
        if (foundTags.isPresent()) {
            // Check existed item
            List<Tags> checkTagExist = repository.findByName(name.trim());
            if (checkTagExist.size() > 0) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "Tag's name already taken", "")
                );
            }

            Tags existTag = foundTags.get();
            existTag.setName(name.trim());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Updated tag successfully", repository.save(existTag))
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can not find tag with id = "+id, "")
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteTag(@PathVariable String id) {
        Optional<Tags> foundTags = repository.findById(id);
        if (foundTags.isPresent()) {
            fkTagsNewsRepository.deleteByFkTags(foundTags.get().getId());
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Deleted tag successfully", foundTags)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can not find tag with id = "+id, "")
        );
    }
}
