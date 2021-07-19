package com.example.our_stories.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.our_stories.enums.Genre;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class Story {
    @PrimaryKey
    @NonNull
    private Long id;
    @NonNull
    private Long authorId;
    @NonNull
    private String Name;
    private String summary;
    private String imagePath;
    private List<Genre> ganres;
    private List<Chapter> chapters;
    private List<Comment> comments;
}
