package com.Movies.repository;

import com.Movies.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    public List<Review> getReviewsByEmail(String email);

}
