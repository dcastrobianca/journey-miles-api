package com.journey.miles.api.domain.review;

import com.journey.miles.api.domain.review.dto.ReviewData;
import com.journey.miles.api.domain.review.dto.ReviewDetailsData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class ReviewServiceTest {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ReviewRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void shouldCreateReview() {
        //given
        String name = "Fulano da Silva";
        String description = "this is my option about journey miles";
        String photoPath = "my/photo/path";
        ReviewData reviewData = new ReviewData(name, description, photoPath);

        //when
        ReviewDetailsData reviewDetails = reviewService.create(reviewData);

        //then
        assertEquals(1.0, repository.count());
        assertThat(reviewDetails.fullName()).isEqualTo(name);
        assertThat(reviewDetails.description()).isEqualTo(description);
        assertThat(reviewDetails.photoPath()).isEqualTo(photoPath);
    }

    @Test
    void shouldReturnReviewDetailsListWith2Elements(){
        //given
        String name = "Fulano da Silva";
        String description = "this is my option about journey miles";
        String photoPath = "my/photo/path";
        ReviewData reviewData = new ReviewData(name, description, photoPath);
        reviewService.create(reviewData);
        reviewService.create(reviewData);
        Pageable pageable = PageRequest.of(0,10);

        //when
        Page<ReviewDetailsData> reviewsDetailsList = reviewService.findAll(pageable);
        //then
        assertEquals(2, reviewsDetailsList.getTotalElements());
    }

    @Test
    void shouldReturnEmptyReviewDetailsList(){
        //given
        Pageable pageable = PageRequest.of(0,10);
        //when
        Page<ReviewDetailsData> reviewsDetailsList = reviewService.findAll(pageable);
        //then
        assertEquals(0, reviewsDetailsList.getTotalElements());
    }
}