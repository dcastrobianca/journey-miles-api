package com.journey.miles.api.controller;

import com.journey.miles.api.domain.review.dto.ReviewRegistrationData;
import com.journey.miles.api.domain.review.dto.ReviewDetailsData;
import com.journey.miles.api.domain.review.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService service;

    @PostMapping
    public ResponseEntity<ReviewDetailsData> create(UriComponentsBuilder uriBuilder, @Valid @RequestBody ReviewRegistrationData data) {
        ReviewDetailsData review = service.create(data);
        URI uri = uriBuilder.path("/review/{id}").buildAndExpand(review.id()).toUri();
        return ResponseEntity.created(uri).body(review);
    }

    @GetMapping
    public ResponseEntity<Page<ReviewDetailsData>> findAll(@PageableDefault Pageable pageable){
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDetailsData> findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping
    public ResponseEntity<ReviewDetailsData> update(@RequestBody @Valid ReviewDetailsData data){
        return ResponseEntity.ok(service.update(data));
    }

}
