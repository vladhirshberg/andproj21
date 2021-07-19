package com.example.our_stories.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class Chapter {
    @NonNull
    @PrimaryKey
    private Long chapterNum;
    @NonNull
    @PrimaryKey
    private Long storyId;
    private String content;
    private List<Comment> comments;
}
