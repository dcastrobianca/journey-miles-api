package com.journey.miles.api.domain.review.dto;

import jakarta.validation.constraints.NotBlank;

public record ReviewData(
        @NotBlank
        String fullName,
        @NotBlank
        String description,
        String photoPath) {
}
