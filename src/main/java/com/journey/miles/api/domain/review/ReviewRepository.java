package com.journey.miles.api.domain.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("reviewRepository")
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
