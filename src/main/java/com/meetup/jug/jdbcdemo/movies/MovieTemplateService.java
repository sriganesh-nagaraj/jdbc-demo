package com.meetup.jug.jdbcdemo.movies;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service("MovieTemplateService")
public class MovieTemplateService implements MovieService {

  private JdbcTemplate jdbcTemplate;

  public MovieTemplateService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private Movie rowMapper(final ResultSet rs, final int rowNum)
      throws SQLException {
    return new Movie(
        rs.getString("id"),
        rs.getString("title"),
        rs.getString("genre"),
        rs.getDate("release_date").toLocalDate(),
        rs.getDouble("rating"));
  }

  @Override
  public Optional<Movie> save(Movie movie) {
    String sql = """
      INSERT INTO movies (id, title, genre, release_date, rating) VALUES
      (?, ?, ?, ?, ?)
      """;
    String id = Objects.isNull(movie.id()) ? UUID.randomUUID().toString()
        : movie.id();
    jdbcTemplate.update(sql, id, movie.title(), movie.genre(),
        movie.releaseDate(), movie.rating());
    return findById(id);
  }

  @Override
  public Optional<Movie> findById(String id) {
    String sql = """ 
        SELECT id, title, genre, release_date, rating FROM movies WHERE id = ?
        """;
    try {
      Movie movie = jdbcTemplate.queryForObject(sql, this::rowMapper, id);
      return Optional.ofNullable(movie);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<Movie> findAll() {
    String sql = """ 
        SELECT id, title, genre, release_date, rating FROM movies
        """;
    List<Movie> movies = jdbcTemplate.query(sql, this::rowMapper);
    return movies;
  }

  @Override
  public boolean delete(String id) {
    String sql = "DELETE FROM movies WHERE id = ?";
    return jdbcTemplate.update(sql, id) > 0;
  }

  @Override
  public void deleteAll() {
    String sql = "DELETE FROM movies";
    jdbcTemplate.update(sql);
  }

  @Override
  public Optional<Movie> update(String id, Movie movie) {
    String sql = """
        UPDATE movies
        SET title = ?, genre = ?, release_date = ?, rating = ? WHERE id = ?
        """;
    int rows = jdbcTemplate.update(sql, movie.title(), movie.genre(),
        movie.releaseDate(), movie.rating(), id);
    return findById(id);
  }
}
