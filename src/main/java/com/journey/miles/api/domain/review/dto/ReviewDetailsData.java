package com.journey.miles.api.domain.review.dto;

import com.journey.miles.api.domain.review.Review;
import jakarta.validation.constraints.NotNull;

public record ReviewDetailsData(@NotNull Long id, String fullName, String description, String photoPath) {
    public ReviewDetailsData(Review review) {
        this(review.getId(), review.getName(), review.getDescription(), review.getPhotoPath());
    }
}
