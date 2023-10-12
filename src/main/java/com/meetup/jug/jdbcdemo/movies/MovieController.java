package com.meetup.jug.jdbcdemo.movies;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

  @Autowired
  @Qualifier("MovieClientService")
  private MovieService movieService;


  @GetMapping
  public ResponseEntity<List<Movie>> findAll() {
    List<Movie> movies = movieService.findAll();
    return ResponseEntity.ok(movies);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Movie> findById(@PathVariable String id) {
    Optional<Movie> optionalMovie = movieService.findById(id);
    return optionalMovie.map(ResponseEntity::ok).orElseGet(
        () -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Movie> create(@RequestBody Movie movie) {
    Optional<Movie> createdMovie = movieService.save(movie);
    return createdMovie.map(ResponseEntity::ok).orElseGet(
        () -> ResponseEntity.badRequest().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<Movie> update(
      @PathVariable String id,
      @RequestBody Movie movie) {
    Optional<Movie> updatedMovie = movieService.update(id, movie);
    return updatedMovie.map(ResponseEntity::ok).orElseGet(
        () -> ResponseEntity.badRequest().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    boolean deleted = movieService.delete(id);
    return deleted ? ResponseEntity.ok().build()
        : ResponseEntity.notFound().build();
  }


}
