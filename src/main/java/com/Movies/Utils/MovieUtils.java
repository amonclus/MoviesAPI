package com.Movies.Utils;

import com.Movies.model.Review;

import java.util.List;

public class MovieUtils {

    public static int calculateAvgRating(List<Review> reviews){
        int sum = 0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        return sum / reviews.size();
    }
}
