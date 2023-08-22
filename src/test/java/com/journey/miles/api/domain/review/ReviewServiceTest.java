package com.journey.miles.api.domain.review;

import com.journey.miles.api.domain.review.dto.ReviewData;
import com.journey.miles.api.domain.review.dto.ReviewDetailsData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
}