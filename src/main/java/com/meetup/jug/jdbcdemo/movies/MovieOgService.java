package com.meetup.jug.jdbcdemo.movies;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service("MovieOgService")
public class MovieOgService implements MovieService {

  // JDBC driver name and database URL
  static final String JDBC_DRIVER = "org.h2.Driver";
  static final String DB_URL = "jdbc:h2:~/test";

  //  Database credentials
  static final String USER = "sa";
  static final String PASS = "";

  @Override
  public Optional<Movie> save(Movie movie) {
    return Optional.empty();
  }

  @Override
  public Optional<Movie> findById(String id) {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try {
      Class.forName(JDBC_DRIVER);

      connection = DriverManager.getConnection(DB_URL, USER, PASS);

      String sql = """
          SELECT id, title, genre, release_date, rating
          FROM 
          movies WHERE id = ?
            """;
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, id);
      resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        String resultId = resultSet.getString("id");
        String resultTitle = resultSet.getString("title");
        String resultGenre = resultSet.getString("genre");
        LocalDate resultReleaseDate = resultSet.getDate("release_date")
            .toLocalDate();
        Double resultRating = resultSet.getDouble("rating");
        return Optional.of(new Movie(resultId, resultTitle, resultGenre,
            resultReleaseDate, resultRating));
      } else {
        return Optional.empty();
      }
    } catch (ClassNotFoundException | SQLException e) {
      return Optional.empty();
    } finally {
      try {
        if (connection != null) {
          connection.close();
        }
        if (preparedStatement != null) {
          preparedStatement.close();
        }
        if (resultSet != null) {
          resultSet.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public List<Movie> findAll() {
    return null;
  }

  @Override
  public boolean delete(String id) {
    return false;
  }

  @Override
  public void deleteAll() {

  }

  @Override
  public Optional<Movie> update(String id, Movie movie) {
    return Optional.empty();
  }
}
