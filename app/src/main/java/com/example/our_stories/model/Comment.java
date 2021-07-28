package com.example.our_stories.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class Comment {
    @NonNull
    @PrimaryKey
    private String id;
    @NonNull
    private String storyId;
    private Long chapterNum;
    private String content;
    private String userId;
    private String date;
}
