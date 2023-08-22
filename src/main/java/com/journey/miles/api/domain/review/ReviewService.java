package com.journey.miles.api.domain.review;

import com.journey.miles.api.domain.review.dto.ReviewData;
import com.journey.miles.api.domain.review.dto.ReviewDetailsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository repository;

    public ReviewDetailsData create(ReviewData data) {
        Review review = new Review(data);
        repository.save(review);
        return new ReviewDetailsData(review);
    }

    public List<ReviewDetailsData> findAll() {
        List<Review> reviews = repository.findAll();
        return reviews.stream().map(ReviewDetailsData::new).toList();
    }
}
