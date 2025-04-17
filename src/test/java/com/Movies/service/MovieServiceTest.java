package com.Movies.service;

import com.Movies.exception.NotFoundException;
import com.Movies.exception.ReviewNotFoundException;
import com.Movies.model.Movie;
import com.Movies.model.Review;
import com.Movies.repository.MovieRepository;
import com.Movies.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {
    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private MovieService movieService;

    private Movie movie;
    private Review review;

    @BeforeEach
    public void setUp() {
        movie = new Movie();
        review = new Review();
        List<Review> reviews = new ArrayList<>();

        movie.setId(1L);
        movie.setName("The Dark Knight");
        movie.setDuration("120");
        movie.setGenre("Action");

        review.setId(1L);
        review.setEmail("test@email.com");
        review.setRating(80);
        review.setMovie(movie);

        reviews.add(review);
        movie.setReviews(reviews);
    }

    @Test
    public void testGetAllMovies() {
        List<Movie> movies = new ArrayList<>();
        movies.add(movie);

        when(movieRepository.findAll()).thenReturn(movies);

        List<Movie> movieRes = movieService.getAllMovies();
        assertEquals(1, movieRes.size());
        assertEquals(movie.getName(), movieRes.get(0).getName());
    }

    @Test
    public void testGetMovieById() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        Movie movieRes = movieService.getMovieById(1L);
        assertEquals(movie.getName(), movieRes.getName());
    }

    @Test
    public void testGetMovieById_NotFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> movieService.getMovieById(1L));
    }

    @Test
    public void testGetReviewsByMovieId() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        List<Review> reviews = movieService.getReviewsByMovieId(1L);
        assertEquals(1, reviews.size());
        assertEquals(review.getEmail(), reviews.get(0).getEmail());
    }

    @Test
    public void testGetReviewsByMovieId_NotFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> movieService.getReviewsByMovieId(1L));
    }

    @Test
    public void testGetReviewsByEmail() {
        List<Review> reviews = new ArrayList<>();
        reviews.add(review);
        when(reviewRepository.getReviewsByEmail(review.getEmail())).thenReturn(reviews);
        List<Review> reviewsRes = movieService.getReviewsByEmail(review.getEmail());
        assertEquals(1, reviewsRes.size());
        assertEquals(review.getEmail(), reviewsRes.get(0).getEmail());
    }

    @Test
    public void testGetReviewByMovieIdAndReviewId() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        Review reviewRes = movieService.getReviewByMovieIdAndReviewId(1L, 1L);
        assertEquals(review.getDescription(), reviewRes.getDescription());
    }

    @Test
    public void testGetReviewByMovieIdAndReviewId_NotFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        assertThrows(ReviewNotFoundException.class, () -> movieService.getReviewByMovieIdAndReviewId(1L, 2L));
    }

    @Test
    public void testGetMovieRating() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        int rating = movieService.getMovieRating(1L);
        assertEquals(80, rating);
    }

    @Test
    public void testAddMovie() {
        when(movieRepository.save(movie)).thenReturn(movie);
        Movie movieRes = movieService.addMovie(movie);
        assertEquals(movie.getName(), movieRes.getName());
    }

    @Test
    public void testUpdateMovie() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.save(movie)).thenReturn(movie);

        Movie updatedMovie = new Movie();
        updatedMovie.setName("Updated Movie");

        Movie movieRes = movieService.updateMovie(1L, updatedMovie);
        assertEquals("Updated Movie", movieRes.getName());
    }

    @Test
    public void testUpdateReviewByMovieIdAndReviewId() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.save(movie)).thenReturn(movie);

        Review updatedReview = new Review();
        updatedReview.setDescription("Updated Review");

        Movie movieRes = movieService.updateReviewByMovieIdAndReviewId(1L, 1L, updatedReview);
        assertEquals("Updated Review", movieRes.getReviews().get(0).getDescription());
    }

    @Test
    public void testDeleteMovie() {
        movieService.deleteMovie(1L);
        verify(movieRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteReviewByMovieIdAndReviewId() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.save(movie)).thenReturn(movie);

        Movie movieRes = movieService.deleteReviewByMovieIdAndReviewId(1L, 1L);
        assertTrue(movieRes.getReviews().isEmpty());
    }
}