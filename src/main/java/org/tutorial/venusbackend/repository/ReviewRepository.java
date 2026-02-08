package org.tutorial.venusbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tutorial.venusbackend.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}