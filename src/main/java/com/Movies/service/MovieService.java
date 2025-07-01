package com.Movies.service;

import com.Movies.Utils.MovieUtils;
import com.Movies.exception.NotFoundException;
import com.Movies.exception.ReviewNotFoundException;
import com.Movies.model.Movie;
import com.Movies.model.Review;
import com.Movies.repository.MovieRepository;
import com.Movies.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(Long movie_id) {
        return movieRepository.findById(movie_id).orElseThrow(() -> new NotFoundException("Movie not found with id: " + movie_id));
    }

    public List<Review> getReviewsByMovieId(Long movie_id) {
        Movie movie = movieRepository.findById(movie_id).orElseThrow(() -> new NotFoundException("Movie not found with id: " + movie_id));
            return movie.getReviews();
    }

    public List<Review> getReviewsByEmail(String email) {
        return reviewRepository.getReviewsByEmail(email);
    }

    public Review getReviewByMovieIdAndReviewId(Long movie_id, Long reviewId) {
        Movie movie = movieRepository.findById(movie_id).orElseThrow(() -> new NotFoundException("Movie not found with id: " + movie_id));

        return movie.getReviews().stream()
                .filter(review -> review.getId().equals(reviewId))
                .findFirst()
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + reviewId));
    }

    public int getMovieRating(Long movie_id){
        Movie movie = movieRepository.findById(movie_id).orElseThrow(() -> new NotFoundException("Movie not found with id: " + movie_id));

        return MovieUtils.calculateAvgRating(movie.getReviews());
    }

    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public Movie addReviewToMovie(Long movie_id, Review review) {
        Movie movie = movieRepository.findById(movie_id).orElseThrow(() -> new NotFoundException("Movie not found with id: " + movie_id));

        movie.getReviews().add(review);
        review.setMovie(movie);
        movieRepository.save(movie);
        return movie;

    }

    public Movie updateMovie(Long movie_id, Movie movie) {
        Movie existingMovie = movieRepository.findById(movie_id).orElseThrow(() -> new NotFoundException("Movie not found with id: " + movie_id));

        if (movie.getName() != null) {
            existingMovie.setName(movie.getName());
        }
        if (movie.getDuration() != null) {
            existingMovie.setDuration(movie.getDuration());
        }
        if (movie.getGenre() != null) {
            existingMovie.setGenre(movie.getGenre());
        }
        return movieRepository.save(existingMovie);

    }

    public Movie updateReviewByMovieIdAndReviewId(Long movie_id, Long reviewId, Review review) {
        Movie movie = movieRepository.findById(movie_id).orElseThrow(() -> new NotFoundException("Movie not found with id: " + movie_id));
        Review existingReview = movie.getReviews().stream().filter(r -> r.getId().equals(reviewId)).findFirst().orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + reviewId));

        if (review.getEmail() != null) {
            existingReview.setEmail(review.getEmail());
        }
        if (review.getDescription() != null) {
            existingReview.setDescription(review.getDescription());
        }
        if(review.getRating() != 0) {
            existingReview.setRating(review.getRating());
        }
        return movieRepository.save(movie);
    }

    public void deleteMovie(Long movie_id) {
        Movie movie = movieRepository.findById(movie_id)
            .orElseThrow(() -> new NotFoundException("Movie not found with id: " + movie_id));
        movieRepository.deleteById(movie_id);
    }

    public Movie deleteReviewByMovieIdAndReviewId(Long movie_id, Long reviewId) {
        Movie movie = movieRepository.findById(movie_id).orElseThrow(() -> new NotFoundException("Movie not found with id: " + movie_id));
        Review review = movie.getReviews().stream().filter(r -> r.getId().equals(reviewId)).findFirst().orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + reviewId));

        movie.getReviews().remove(review);
        movieRepository.save(movie);
        return movie;
    }

    public Movie getMovieByName(String name) {
        return movieRepository.findByName(name);
    }

    public List<Movie> getMoviesByCriteria(String name, String duration, String genre) {
        Specification<Movie> spec = Specification.where(null);

        if (name != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("name"), name));
        }
        if (duration != null) {
            try {
                int durationInt = Integer.parseInt(duration);
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.greaterThanOrEqualTo(root.get("duration"), durationInt));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid duration format");
            }
        }
        if (genre != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("genre"), genre));
        }

        return movieRepository.findAll(spec);
    }
}