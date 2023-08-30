package com.journey.miles.api.domain.review;

import com.journey.miles.api.domain.review.dto.ReviewData;
import com.journey.miles.api.domain.review.dto.ReviewDetailsData;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public ReviewDetailsData findById(Long id) {
        Optional<Review> review = repository.findById(id);
        if(review.isEmpty()){
            throw new EntityNotFoundException("Could not found review with this id.");
        }
        return new ReviewDetailsData(review.get());
    }
}
