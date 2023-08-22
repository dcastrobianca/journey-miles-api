package com.journey.miles.api.controller;

import com.journey.miles.api.domain.review.dto.ReviewData;
import com.journey.miles.api.domain.review.dto.ReviewDetailsData;
import com.journey.miles.api.domain.review.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService service;

    @PostMapping
    public ResponseEntity<ReviewDetailsData> create(UriComponentsBuilder uriBuilder, @Valid @RequestBody ReviewData data) {
        ReviewDetailsData review = service.create(data);
        URI uri = uriBuilder.path("/review/{id}").buildAndExpand(review.id()).toUri();
        return ResponseEntity.created(uri).body(review);
    }

}
