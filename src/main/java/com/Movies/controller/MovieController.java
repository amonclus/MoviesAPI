package com.Movies.controller;

import com.Movies.model.Movie;
import com.Movies.model.Review;
import com.Movies.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping
    public List<Movie> getAllMovies(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String duration,
            @RequestParam(required = false) String genre) {
        if(name != null || genre != null || duration != null) {
            return movieService.getMoviesByCriteria(name, duration, genre);
        }
        return movieService.getAllMovies();
    }

    @GetMapping("/{movie_id}")
    public Movie getMovieById(@PathVariable Long movie_id) {
        return movieService.getMovieById(movie_id);
    }

    @GetMapping("/name/{name}")
    public Movie getMovieByName(@PathVariable String name) {
        return movieService.getMovieByName(name);
    }

    @GetMapping("/{movie_id}/reviews")
    public List<Review> getReviewsByMovieId(@PathVariable Long movie_id) {
        return movieService.getReviewsByMovieId(movie_id);
    }

    @GetMapping("/reviews/{email}")
    public List<Review> getReviewsByEmail(@PathVariable String email) {
        return movieService.getReviewsByEmail(email);
    }

    @GetMapping("/{movie_id}/reviews/{review_id}")
    public Review getReviewByMovieIdAndReviewId(@PathVariable Long movie_id, @PathVariable Long review_id) {
        return movieService.getReviewByMovieIdAndReviewId(movie_id, review_id);
    }

    @GetMapping("/{movie_id}/rating")
    public int getMovieRating(@PathVariable Long movie_id) {
        return movieService.getMovieRating(movie_id);
    }

    @PostMapping
    public Movie addMovie(@RequestBody Movie movie) {
        return movieService.addMovie(movie);
    }

    @PostMapping("/{movie_id}/reviews")
    public Movie addReviewToMovie(@PathVariable Long movie_id, @RequestBody Review review) {
        return movieService.addReviewToMovie(movie_id, review);
    }

    @PutMapping("/{movie_id}")
    public Movie updateMovie(@PathVariable Long movie_id, @RequestBody Movie movie) {
        return movieService.updateMovie(movie_id, movie);
    }

    @PutMapping("/{movie_id}/reviews/{review_id}")
    public Movie updateReviewByMovieIdAndReviewId(@PathVariable Long movie_id, @PathVariable Long review_id, @RequestBody Review review) {
        return movieService.updateReviewByMovieIdAndReviewId(movie_id, review_id, review);
    }

    @DeleteMapping("/{movie_id}")
    public void deleteMovie(@PathVariable Long movie_id) {
        movieService.deleteMovie(movie_id);
    }

    @DeleteMapping("/{movie_id}/reviews/{review_id}")
    public Movie deleteReviewByMovieIdAndReviewId(@PathVariable Long movie_id, @PathVariable Long review_id) {
        return movieService.deleteReviewByMovieIdAndReviewId(movie_id, review_id);
    }
}