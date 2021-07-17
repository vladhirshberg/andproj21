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
public class User {
    @PrimaryKey
    @NonNull
    private Long userid;
    private String username;
    private String email;
    private String password;
    private String imagePath;
}
