package com.journey.miles.api.domain.review;

import com.journey.miles.api.domain.review.dto.ReviewRegistrationData;
import com.journey.miles.api.domain.review.dto.ReviewDetailsData;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        ReviewRegistrationData reviewRegistrationData = new ReviewRegistrationData(name, description, photoPath);

        //when
        ReviewDetailsData reviewDetails = reviewService.create(reviewRegistrationData);

        //then
        assertEquals(1.0, repository.count());
        assertThat(reviewDetails.fullName()).isEqualTo(name);
        assertThat(reviewDetails.description()).isEqualTo(description);
        assertThat(reviewDetails.photoPath()).isEqualTo(photoPath);
    }

    @Test
    void shouldReturnReviewDetailsListWith2Elements() {
        //given
        String name = "Fulano da Silva";
        String description = "this is my option about journey miles";
        String photoPath = "my/photo/path";
        ReviewRegistrationData reviewRegistrationData = new ReviewRegistrationData(name, description, photoPath);

        reviewService.create(reviewRegistrationData);
        reviewService.create(reviewRegistrationData);
        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<ReviewDetailsData> reviewsDetailsList = reviewService.findAll(pageable);
        //then
        assertEquals(2, reviewsDetailsList.getTotalElements());
    }

    @Test
    void shouldReturnEmptyReviewDetailsList() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        //when
        Page<ReviewDetailsData> reviewsDetailsList = reviewService.findAll(pageable);
        //then
        assertEquals(0, reviewsDetailsList.getTotalElements());
    }

    @Test
    void shouldReturnReviewDetailsWhenFoundReviewById() {
        //given
        String name = "Fulano da Silva";
        String description = "this is my option about journey miles";
        String photoPath = "my/photo/path";
        ReviewRegistrationData reviewRegistrationData = new ReviewRegistrationData(name, description, photoPath);

        ReviewDetailsData newReviewDetails = reviewService.create(reviewRegistrationData);

        //when
        ReviewDetailsData reviewDetailsFoundById = reviewService.findById(newReviewDetails.id());

        //then
        assertThat(reviewDetailsFoundById).isEqualTo(newReviewDetails);
    }

    @Test
    void shouldReturnEntityNotFoundExceptionWhenThereIsNoReviewWithID() {
        //given
        Long id = 1L;

        //when and then
        assertThrows(EntityNotFoundException.class, () -> reviewService.findById(id));
    }

    @Test
    void shouldReturnReviewNewDetailsWhenUpdateName() {
        String oldFullName = "Fulano";
        String newFullName = "Ciclano";
        String description = "this is my review";
        String photoPath = "/my/photo.png";
        ReviewDetailsData oldReview = reviewService.create(new ReviewRegistrationData(oldFullName, description, photoPath));
        ReviewDetailsData newReviewDetails = new ReviewDetailsData(oldReview.id(), newFullName, null, null);

        //when
        ReviewDetailsData reviewDetails = reviewService.update(newReviewDetails);

        //then
        assertThat(reviewDetails.fullName()).isEqualTo(newFullName);
        assertThat(reviewDetails.description()).isEqualTo(description);
        assertThat(reviewDetails.photoPath()).isEqualTo(photoPath);
    }

    @Test
    void shouldReturnReviewNewDetailsWhenUpdateDescription() {
        String name = "Ciclano";
        String oldDescription = "this is my review";
        String newDescription = "this is my new review";
        String photoPath = "/my/photo.png";
        ReviewDetailsData oldReview = reviewService.create(new ReviewRegistrationData(name, oldDescription, photoPath));
        ReviewDetailsData newReviewDetails = new ReviewDetailsData(oldReview.id(), null, newDescription, null);

        //when
        ReviewDetailsData reviewDetails = reviewService.update(newReviewDetails);

        //then
        assertThat(reviewDetails.fullName()).isEqualTo(name);
        assertThat(reviewDetails.description()).isEqualTo(newDescription);
        assertThat(reviewDetails.photoPath()).isEqualTo(photoPath);
    }

    @Test
    void shouldReturnReviewNewDetailsWhenUpdatePhotopath() {
        String name = "Ciclano";
        String description = "this is my review";
        String oldPhotoPath = "/my/photo.png";
        String newPhotoPath = "/my/new/photo.png";
        ReviewDetailsData oldReview = reviewService.create(new ReviewRegistrationData(name, description, oldPhotoPath));
        ReviewDetailsData newReviewDetails = new ReviewDetailsData(oldReview.id(), null, null, newPhotoPath);

        //when
        ReviewDetailsData reviewDetails = reviewService.update(newReviewDetails);

        //then
        assertThat(reviewDetails.fullName()).isEqualTo(name);
        assertThat(reviewDetails.description()).isEqualTo(description);
        assertThat(reviewDetails.photoPath()).isEqualTo(newPhotoPath);
    }

    @Test
    void shouldDeleteReviewWhenReceiveExistingId() {
        //given
        String name = "Ciclano";
        String description = "this is my review";
        String photoPath = "/my/photo.png";
        ReviewDetailsData reviewDetailsData = reviewService.create(new ReviewRegistrationData(name, description, photoPath));
        assertThat(repository.count()).isEqualTo(1);
        //when
        reviewService.delete(reviewDetailsData.id());
        //then
        assertThat(repository.count()).isZero();
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenReceiveNoneExistingID() {
        //given
        Long id = 1L;

        //when and then
        assertThrows(EntityNotFoundException.class, () -> reviewService.delete(id));
    }


}