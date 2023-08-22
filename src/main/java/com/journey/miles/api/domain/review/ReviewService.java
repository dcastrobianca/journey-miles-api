package com.journey.miles.api.domain.review;

import com.journey.miles.api.domain.review.dto.ReviewData;
import com.journey.miles.api.domain.review.dto.ReviewDetailsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository repository;

    public ReviewDetailsData create(ReviewData data) {
        Review review = new Review(data);
        repository.save(review);
        return new ReviewDetailsData(review);
    }

    public Page<ReviewDetailsData> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(ReviewDetailsData::new);
    }
}
