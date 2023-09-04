package com.journey.miles.api.domain.review;

import com.journey.miles.api.domain.review.dto.ReviewDetailsData;
import com.journey.miles.api.domain.review.dto.ReviewRegistrationData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reviews")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String photoPath;

    public Review(ReviewRegistrationData data) {
        this.name = data.fullName();
        this.description = data.description();
        this.photoPath = data.photoPath();
    }

    public void update(ReviewDetailsData data) {
        if (data.fullName() != null) {
            this.name = data.fullName();
        }
        if (data.description() != null) {
            this.description = data.description();
        }
        if (data.photoPath() != null) {
            this.photoPath = data.photoPath();
        }

    }
}
