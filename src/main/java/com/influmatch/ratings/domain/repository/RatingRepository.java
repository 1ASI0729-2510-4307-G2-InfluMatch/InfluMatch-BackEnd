package com.influmatch.ratings.domain.repository;

import com.influmatch.ratings.domain.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {}
