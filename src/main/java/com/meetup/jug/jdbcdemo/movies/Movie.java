package com.meetup.jug.jdbcdemo.movies;

import java.time.LocalDate;

public record Movie(String id, String title, String genre,
                    LocalDate releaseDate, Double rating) {


}
