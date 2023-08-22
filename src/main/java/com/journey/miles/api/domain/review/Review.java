package com.journey.miles.api.domain.review;

import com.journey.miles.api.domain.review.dto.ReviewData;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reviews")
@NoArgsConstructor
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String photoPath;

    public Review(ReviewData data) {
        this.name = data.fullName();
        this.description = data.description();
        this.photoPath = data.photoPath();
    }
}
