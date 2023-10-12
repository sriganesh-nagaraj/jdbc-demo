package com.meetup.jug.jdbcdemo.movies;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

@Service("MovieClientService")
public class MovieClientService implements MovieService {

  private JdbcClient jdbcClient;

  public MovieClientService(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

//  private Movie rowMapper(final ResultSet rs, final int rowNum)
//      throws SQLException {
//    return new Movie(
//        rs.getString("id"),
//        rs.getString("title"),
//        rs.getString("genre"),
//        rs.getDate("release_date").toLocalDate(),
//        rs.getDouble("rating"));
//  }

  @Override
  public Optional<Movie> save(Movie movie) {
    String sql = """
      INSERT INTO movies (id, title, genre, release_date, rating) VALUES
      (?, ?, ?, ?, ?)
      """;
    String id = Objects.isNull(movie.id()) ? UUID.randomUUID().toString()
        : movie.id();
    jdbcClient.sql(sql)
        .param(1, id)
        .param(2, movie.title())
        .param(3, movie.genre())
        .param(4, movie.releaseDate())
        .param(5, movie.rating())
        .update();
    return findById(id);
  }

  @Override
  public Optional<Movie> findById(String id) {
    String sql = """ 
        SELECT id, title, genre, release_date, rating FROM movies WHERE id = ?
        """;

    return jdbcClient.sql(sql).param(1, id).query(Movie.class)
        .optional();
  }

  @Override
  public List<Movie> findAll() {
    String sql = """ 
        SELECT id, title, genre, release_date, rating FROM movies
        """;
    return jdbcClient.sql(sql).query(Movie.class).list();
  }

  @Override
  public boolean delete(String id) {
    String sql = "DELETE FROM movies WHERE id = ?";
    return jdbcClient.sql(sql).param(1, id).update() > 0;
  }

  @Override
  public void deleteAll() {
    String sql = "DELETE FROM movies";
    jdbcClient.sql(sql).update();
  }

  @Override
  public Optional<Movie> update(String id, Movie movie) {
    String sql = """
        UPDATE movies
        SET title = ?, genre = ?, release_date = ?, rating = ? WHERE id = ?
        """;
    jdbcClient.sql(sql)
        .param(1, movie.title())
        .param(2, movie.genre())
        .param(3, movie.releaseDate())
        .param(4, movie.rating())
        .param(5, id)
        .update();
    return findById(id);
  }
}
