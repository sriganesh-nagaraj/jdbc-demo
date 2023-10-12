package com.meetup.jug.jdbcdemo.movies;

import java.util.List;
import java.util.Optional;

public interface MovieService {

  Optional<Movie> save(Movie movie);

  Optional<Movie> findById(String id);

  List<Movie> findAll();

  boolean delete(String id);

  void deleteAll();

  Optional<Movie> update(String id, Movie movie);

}
