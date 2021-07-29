package com.example.our_stories.model;

import androidx.room.TypeConverter;

import com.example.our_stories.enums.Genre;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Converter {
    // Room converters
    @TypeConverter
    public String genreToString(List<Genre> genres) {
        String value = "";
        if (genres != null) {
            for (Genre genre : genres)
                value += genre.name() + ",";
        }

        return value;
    }

    @TypeConverter
    public List<Genre> stringToGenre(String value) {
        List<Genre> genres = new ArrayList<Genre>();
        if (!value.isEmpty()) {
            for (String genre : Arrays.asList(value.split(","))) {
                genres.add(Genre.valueOf(genre));
            }
        }
        return genres;
    }
}