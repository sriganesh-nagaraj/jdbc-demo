package com.meetup.jug.jdbcdemo.movies;

import com.github.javafaker.Faker;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MovieServiceTest {

  @Autowired
  @Qualifier("MovieTemplateService")
  private MovieService movieService;


  @BeforeEach
  public void setUp() {
    movieService.deleteAll();
  }

  private Movie generateRandomMovie() {
    Faker faker = new Faker();
    return new Movie(
        UUID.randomUUID().toString(),
        faker.book().title(),
        faker.book().genre(),
        faker.date().birthday().toInstant()
            .atZone(ZoneId.of("UTC")).toLocalDate(),
        faker.number().randomDouble(2, 0, 10)
    );
  }

  @Test
  public void createMovie() {
    Movie movie = generateRandomMovie();
    Optional<Movie> savedMovie = movieService.save(movie);

    assert savedMovie.isPresent();
  }

  @Test
  public void updateMovie() {
    Movie movie = generateRandomMovie();
    Optional<Movie> savedMovie = movieService.save(movie);

    Movie movieToBeUpdated = generateRandomMovie();
    Optional<Movie> updatedMovie = movieService.update(savedMovie.get().id(),
        movieToBeUpdated);

    assert updatedMovie.isPresent();
    areEqual(movieToBeUpdated, updatedMovie);
  }

  @Test
  public void deleteMovie() {
    Movie movie = generateRandomMovie();
    Optional<Movie> savedMovie = movieService.save(movie);

    boolean deleted = movieService.delete(savedMovie.get().id());
    assert deleted;
    assert movieService.findById(savedMovie.get().id()).isEmpty();
  }

  @Test
  public void deleteAllMovies() {
    Movie movie = generateRandomMovie();
    Movie movie1 = generateRandomMovie();
    Movie movie2 = generateRandomMovie();
    movieService.save(movie);
    movieService.save(movie1);
    movieService.save(movie2);

    movieService.deleteAll();
    assert movieService.findAll().isEmpty();
  }

  @Test
  public void findAllMovies() {
    Movie movie = generateRandomMovie();
    Movie movie1 = generateRandomMovie();
    Movie movie2 = generateRandomMovie();
    movieService.save(movie);
    movieService.save(movie1);
    movieService.save(movie2);

    List<Movie> movies = movieService.findAll();
    assert movies.size() == 3;
  }

  @Test
  public void findByIdMovie() {
    Movie movie = generateRandomMovie();
    Optional<Movie> savedMovie = movieService.save(movie);

    assert savedMovie.isPresent();
    Optional<Movie> foundMovie = movieService.findById(savedMovie.get().id());
    assert foundMovie.isPresent();
    assert foundMovie.get().id().equals(savedMovie.get().id());
    areEqual(savedMovie.get(), foundMovie);
  }

  private static void areEqual(Movie movieToBeUpdated,
      Optional<Movie> updatedMovie) {
    assert updatedMovie.get().title().equals(movieToBeUpdated.title());
    assert updatedMovie.get().genre().equals(movieToBeUpdated.genre());
    assert updatedMovie.get().releaseDate().equals(movieToBeUpdated.releaseDate());
    assert updatedMovie.get().rating().equals(movieToBeUpdated.rating());
  }


}
