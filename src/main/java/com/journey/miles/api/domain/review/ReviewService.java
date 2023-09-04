package com.journey.miles.api.domain.review;

import com.journey.miles.api.domain.review.dto.ReviewRegistrationData;
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

    public ReviewDetailsData create(ReviewRegistrationData data) {
        Review review = new Review(data);
        repository.save(review);
        return new ReviewDetailsData(review);
    }

    public Page<ReviewDetailsData> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(ReviewDetailsData::new);
    }

    public ReviewDetailsData findById(Long id) {
        Optional<Review> review = repository.findById(id);
        if (review.isEmpty()) {
            throw new EntityNotFoundException("Could not found review with this id.");
        }
        return new ReviewDetailsData(review.get());
    }

    public ReviewDetailsData update(ReviewDetailsData data) {
        Optional<Review> reviewOptional = repository.findById(data.id());
        if (reviewOptional.isEmpty()) {
            throw new EntityNotFoundException("Could not found review with this id.");
        }
        Review review = reviewOptional.get();
        review.update(data);
        return new ReviewDetailsData(review);
    }

    public void delete(Long id) {
        Optional<Review> reviewOptional = repository.findById(id);
        if (reviewOptional.isEmpty()) {
            throw new EntityNotFoundException("Could not found review with this id.");
        }
        repository.deleteById(id);
    }
}
