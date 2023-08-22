package com.journey.miles.api.domain.review.dto;

import com.journey.miles.api.domain.review.Review;

public record ReviewDetailsData(Long id, String fullName, String description, String photoPath) {
    public ReviewDetailsData(Review review) {
        this(review.getId(), review.getName(), review.getDescription(), review.getPhotoPath());
    }
}
