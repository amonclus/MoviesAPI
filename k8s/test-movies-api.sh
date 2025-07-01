#!/bin/sh
set -e

API_BASE="http://movies-api-service:80/movies"

echo "=== Movies API Test Job Started at $(date) ==="

# Test connectivity first
echo "Testing connectivity to movies-api-service..."
curl -s --connect-timeout 10 "http://movies-api-service:80/movies" || echo "Failed to connect to service"

# Test 1: Create a new movie
echo "Creating a new movie..."
MOVIE_DATA="{\"name\":\"Test Movie $(date +%s)\",\"duration\":\"120 min\",\"genre\":\"Action\"}"
MOVIE_RESPONSE=$(curl -s -X POST \
  -H "Content-Type: application/json" \
  -d "$MOVIE_DATA" \
  "$API_BASE" || echo "Failed to create movie")

echo "Movie creation response: $MOVIE_RESPONSE"

# Extract movie ID
MOVIE_ID=$(echo "$MOVIE_RESPONSE" | grep -o '"id":[0-9]*' | grep -o '[0-9]*' || echo "")

if [ -n "$MOVIE_ID" ]; then
  echo "Created movie with ID: $MOVIE_ID"

  # Test 2: Add reviews
  echo "Adding reviews to movie $MOVIE_ID..."
  REVIEW1="{\"email\":\"tester1@example.com\",\"description\":\"Great test movie!\",\"rating\":5}"
  curl -s -X POST \
    -H "Content-Type: application/json" \
    -d "$REVIEW1" \
    "$API_BASE/$MOVIE_ID/reviews" || echo "Failed to add review 1"

  REVIEW2="{\"email\":\"tester2@example.com\",\"description\":\"Decent action movie\",\"rating\":4}"
  curl -s -X POST \
    -H "Content-Type: application/json" \
    -d "$REVIEW2" \
    "$API_BASE/$MOVIE_ID/reviews" || echo "Failed to add review 2"

  # Test 3: Get movie with reviews
  echo "Fetching movie with reviews..."
  curl -s "$API_BASE/$MOVIE_ID" || echo "Failed to fetch movie"

  # Test 4: Get movie reviews
  echo "Fetching movie reviews..."
  REVIEWS_RESPONSE=$(curl -s "$API_BASE/$MOVIE_ID/reviews" || echo "Failed to fetch reviews")
  echo "Reviews: $REVIEWS_RESPONSE"

  # Test 5: Update movie
  echo "Updating movie $MOVIE_ID..."
  UPDATED_MOVIE="{\"name\":\"Updated Test Movie $(date +%s)\",\"duration\":\"135 min\",\"genre\":\"Thriller\"}"
  curl -s -X PUT \
    -H "Content-Type: application/json" \
    -d "$UPDATED_MOVIE" \
    "$API_BASE/$MOVIE_ID" || echo "Failed to update movie"

  # Test 6: Get movie rating
  echo "Getting movie rating..."
  curl -s "$API_BASE/$MOVIE_ID/rating" || echo "Failed to get rating"

  # Test 7: Update a review
  REVIEW_ID=$(echo "$REVIEWS_RESPONSE" | grep -o '"id":[0-9]*' | head -1 | grep -o '[0-9]*' || echo "")
  if [ -n "$REVIEW_ID" ]; then
    echo "Updating review $REVIEW_ID..."
    UPDATED_REVIEW="{\"email\":\"tester1@example.com\",\"description\":\"Updated: Excellent test movie!\",\"rating\":5}"
    curl -s -X PUT \
      -H "Content-Type: application/json" \
      -d "$UPDATED_REVIEW" \
      "$API_BASE/$MOVIE_ID/reviews/$REVIEW_ID" || echo "Failed to update review"
  fi
else
  echo "Failed to extract movie ID from response"
fi

# Test 8: Get all movies
echo "Fetching all movies..."
curl -s "$API_BASE" || echo "Failed to fetch all movies"

# Test 9: Search movies by genre
echo "Searching movies by genre..."
curl -s "$API_BASE?genre=Action" || echo "Failed to search by genre"

echo "=== Movies API Test Job Completed at $(date) ==="